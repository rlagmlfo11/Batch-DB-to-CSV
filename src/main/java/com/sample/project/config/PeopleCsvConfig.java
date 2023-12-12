package com.sample.project.config;

import java.io.IOException;
import java.io.Writer;

import javax.sql.DataSource;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sample.project.entity.People;
import com.sample.project.entity.Player;

// 데이터베이스에있는 테이블과 csv의 항목명이 다를경우. wage도뺌

@Configuration
@EnableBatchProcessing
public class PeopleCsvConfig {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Bean
	public JdbcCursorItemReader<Player> databaseCsvItemReader(DataSource dataSource) {
		JdbcCursorItemReader<Player> databaseReader = new JdbcCursorItemReader<>();
		databaseReader.setDataSource(dataSource);
		databaseReader.setSql("SELECT age, gender, mail, name FROM player");
		databaseReader.setRowMapper(new BeanPropertyRowMapper<>(Player.class));
		return databaseReader;
	}

	@Bean
	public ItemProcessor<Player, People> playerProcessor() {
		return player -> {
			People people = new People();
			people.setPersonalName(player.getName());
			people.setPersonalAge(player.getAge());
			people.setPersonalMail(player.getMail());
			people.setPersonalWage(player.getWage());
			people.setPersonalGender(player.getGender());
			return people;
		};
	}

	@Bean
	public FlatFileItemWriter<People> peopleItemWriter() {
		FlatFileItemWriter<People> writer = new FlatFileItemWriter<>();
		writer.setResource(new FileSystemResource("src/main/resources/PEOPLE.csv"));
		writer.setHeaderCallback(new FlatFileHeaderCallback() {
			@Override
			public void writeHeader(Writer writer) throws IOException {
				writer.write("PersonalName,PersonalAge,PersonalGender,PersonalMail");
			}
		});
		writer.setLineAggregator(new DelimitedLineAggregator<People>() {
			{
				setDelimiter(",");
				setFieldExtractor(new BeanWrapperFieldExtractor<People>() {
					{
						setNames(new String[] { "personalName", "personalAge", "personalGender",
								"personalMail" });
					}
				});
			}
		});

		return writer;
	}

	@Bean
	public Step csvFileStep(StepBuilderFactory stepBuilderFactory, ItemReader<Player> reader,
			ItemProcessor<Player, People> processor, ItemWriter<People> writer) {
		return stepBuilderFactory.get("csvFileStep").<Player, People>chunk(10).reader(reader)
				.processor(processor).writer(writer).build();
	}

	@Bean
	public Job exportPlayerJob(JobBuilderFactory jobBuilderFactory, Step csvFileStep) {
		return jobBuilderFactory.get("exportPlayerJob").incrementer(new RunIdIncrementer())
				.flow(csvFileStep).end().build();
	}

}
