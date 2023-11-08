package com.sample.project.config;

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
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sample.project.entity.Player;

@Configuration
@EnableBatchProcessing
public class batchConfig {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Bean
	public JdbcCursorItemReader<Player> databaseCsvItemReader(DataSource dataSource) {
		JdbcCursorItemReader<Player> databaseReader = new JdbcCursorItemReader<>();
		databaseReader.setDataSource(dataSource);
		databaseReader.setSql("SELECT id, age, gender, mail, name FROM player");
		databaseReader.setRowMapper(new BeanPropertyRowMapper<>(Player.class));
		return databaseReader;
	}

	@Bean
	public FlatFileItemWriter<Player> csvFileItemWriter() {
		FlatFileItemWriter<Player> csvFileWriter = new FlatFileItemWriter<>();
		csvFileWriter.setResource(new FileSystemResource("src/main/resources/output_Player.csv"));
		csvFileWriter.setAppendAllowed(true);
		csvFileWriter.setLineAggregator(new DelimitedLineAggregator<Player>() {
			{
				setDelimiter(",");
				setFieldExtractor(new BeanWrapperFieldExtractor<Player>() {
					{
						setNames(new String[] { "id", "age", "gender", "mail", "name" });
					}
				});
			}
		});
		return csvFileWriter;
	}

	@Bean
	public Step databaseToCsvFileStep(ItemWriter<Player> csvFileItemWriter,
			ItemReader<Player> databaseCsvItemReader) {
		return stepBuilderFactory.get("databaseToCsvFileStep").<Player, Player>chunk(10)
				.reader(databaseCsvItemReader).writer(csvFileItemWriter).build();
	}

	@Bean
	public JobCompletionNotificationListener jobCompletionNotificationListener() {
		return new JobCompletionNotificationListener();
	}

	@Bean
	public Job databaseToCsvFileJob(JobCompletionNotificationListener listener,
			Step databaseToCsvFileStep) {
		return jobBuilderFactory.get("databaseToCsvFileJob").incrementer(new RunIdIncrementer())
				.listener(listener).flow(databaseToCsvFileStep).end().build();
	}

	// Listener class
	public static class JobCompletionNotificationListener extends JobExecutionListenerSupport {
		@Override
		public void afterJob(JobExecution jobExecution) {
			if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
				System.out.println("DATABASE TO CSV JOB FINISHED SUCCESSFULLY");
			}
		}
	}

}
