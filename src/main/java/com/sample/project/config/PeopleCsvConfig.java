package com.sample.project.config;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

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

import com.sample.project.dto.PlayerRepository;
import com.sample.project.entity.People;
import com.sample.project.entity.Player;

@Configuration
@EnableBatchProcessing
public class PeopleCsvConfig {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	public PlayerRepository playerRepository;

	@Bean
	public JdbcCursorItemReader<Player> databaseCsvItemReader(DataSource dataSource) {
		JdbcCursorItemReader<Player> databaseReader = new JdbcCursorItemReader<>();
		databaseReader.setDataSource(dataSource);
		databaseReader.setSql("SELECT age, gender, mail, name, boss, wage FROM player");
		databaseReader.setRowMapper(new BeanPropertyRowMapper<>(Player.class));
		return databaseReader;
	}

	@Bean
	public ItemProcessor<Player, People> playerProcessor() {
		return new ItemProcessor<Player, People>() {
			@Override
			public People process(Player player) throws Exception {
				People people = new People();
				people.setPersonalName(player.getName());
				people.setPersonalAge(player.getAge());
//				people.setPersonalMail(player.getMail());
				people.setPersonalWage(player.getWage());
				people.setPersonalGender(player.getGender());

				String[] setupParts = player.getBoss().split("\\|");

				for (int i = 0; i < setupParts.length && i < 3; i++) {
					String bossName = setupParts[i];
					String bossMail = playerRepository.findMailByMail(bossName);
					String bossWage = playerRepository.findMailByWage(bossName);

					switch (i) {
					case 0:
						people.setBoss1(bossMail + " | " + bossWage);
						break;
					case 1:
						people.setBoss2(bossMail + " | " + bossWage);
						break;
					case 2:
						people.setBoss3(bossMail + " | " + bossWage);
						break;
					}
				}

//				for (int i = 0; i < setupParts.length && i < 3; i++) {
//					String bossName = setupParts[i];
//					Object[] mailAndWage = playerRepository.findMailAndWageByName(bossName);
//					if (mailAndWage != null && mailAndWage.length == 2) {
//						String combinedInfo = mailAndWage[0] + " " + mailAndWage[1];
//						switch (i) {
//						case 0:
//							people.setBoss1(combinedInfo);
//							break;
//						case 1:
//							people.setBoss2(combinedInfo);
//							break;
//						case 2:
//							people.setBoss3(combinedInfo);
//							break;
//						}
//					}
//				}

//				for (String a : playerRepository.findByNAME()) {
//					if (setupParts.length == 1) {
//						if (setupParts[0].equals(a)) {
//							people.setBoss1(player.getMail());
//						}
//					}
//
//					if (setupParts.length == 2) {
//						people.setBoss1(player.getMail());
//						people.setBoss2(player.getMail());
//					}
//
//					if (setupParts.length == 3) {
//						people.setBoss1(player.getMail());
//						people.setBoss2(player.getMail());
//						people.setBoss3(player.getMail());
//					}
//				}

				return people;
			}
		};
	}

	@Bean
	public FlatFileItemWriter<People> peopleItemWriter() {
		FlatFileItemWriter<People> writer = new FlatFileItemWriter<>();
		writer.setResource(new FileSystemResource("src/main/resources/PEOPLE.csv"));
		writer.setHeaderCallback(new FlatFileHeaderCallback() {
			@Override
			public void writeHeader(Writer writer) throws IOException {
				writer.write("PersonalName,PersonalAge,PersonalGender,Boss1,Boss2,Boss3");
			}
		});
		writer.setLineAggregator(new DelimitedLineAggregator<People>() {
			{
				setDelimiter(",");
				setFieldExtractor(new BeanWrapperFieldExtractor<People>() {
					{
						setNames(new String[] { "personalName", "personalAge", "personalGender",
								"boss1", "boss2", "boss3" });
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
