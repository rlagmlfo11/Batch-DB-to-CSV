//package com.sample.project.config;
//
//import java.io.IOException;
//import java.io.Writer;
//
//import javax.sql.DataSource;
//
//import org.springframework.batch.core.BatchStatus;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobExecution;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.core.listener.JobExecutionListenerSupport;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.batch.item.database.JdbcCursorItemReader;
//import org.springframework.batch.item.file.FlatFileHeaderCallback;
//import org.springframework.batch.item.file.FlatFileItemWriter;
//import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
//import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.jdbc.core.BeanPropertyRowMapper;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.sample.project.entity.Player;
//
//// 데이터베이스에있는 테이블을 csv 그대로 출력
//
//@Configuration
//@EnableBatchProcessing
//public class DBtoCsvConfig {
//
//	@Autowired
//	public JobBuilderFactory jobBuilderFactory;
//
//	@Autowired
//	public StepBuilderFactory stepBuilderFactory;
//
//	@Bean
//	public JdbcCursorItemReader<Player> databaseCsvItemReader(DataSource dataSource) {
//		JdbcCursorItemReader<Player> databaseReader = new JdbcCursorItemReader<>();
//		databaseReader.setDataSource(dataSource);
//		databaseReader.setSql("SELECT age, gender, mail, name,wage FROM player");
//		databaseReader.setRowMapper(new BeanPropertyRowMapper<>(Player.class));
//		return databaseReader;
//	}
//
//	@Bean
//	public FlatFileHeaderCallback headerCallback() {
//		return new FlatFileHeaderCallback() {
//			public void writeHeader(Writer writer) throws IOException {
//				writer.write("AGE,GENDER,MAIL,NAME,WAGE");
//			}
//		};
//	}
//
//	@Bean
//	public FlatFileItemWriter<Player> csvFileWriter() {
//		FlatFileItemWriter<Player> writer = new FlatFileItemWriter<>();
//		writer.setResource(new FileSystemResource("src/main/resources/output_Player.csv"));
//		writer.setAppendAllowed(false);
//		writer.setHeaderCallback(headerCallback());
//
//		writer.setLineAggregator(new DelimitedLineAggregator<Player>() {
//			{
//				setDelimiter(",");
//				setFieldExtractor(new BeanWrapperFieldExtractor<Player>() {
//					{
//						setNames(new String[] { "age", "gender", "mail", "name", "wage" });
//					}
//				});
//			}
//		});
//
//		return writer;
//	}
//
//	@Bean
//	public ItemProcessor<Player, Player> playerFilterProcessor() {
//		return new ItemProcessor<Player, Player>() {
//			@Override
//			public Player process(Player player) throws Exception {
//				return player; // only return players with a wage greater than 50
//			}
//		};
//	}
//
//	@Bean
//	public Step databaseToCsvFileStep(ItemProcessor<Player, Player> playerFilterProcessor,
//			ItemWriter<Player> csvFileItemWriter, ItemReader<Player> databaseCsvItemReader) {
//		return stepBuilderFactory.get("databaseToCsvFileStep").<Player, Player>chunk(10)
//				.reader(databaseCsvItemReader).processor(playerFilterProcessor) // Add the processor
//																				// here
//				.writer(csvFileItemWriter).build();
//	}
//
//	@Bean
//	public Step databaseToCsvFileStep(ItemWriter<Player> csvFileItemWriter,
//			ItemReader<Player> databaseCsvItemReader) {
//		return stepBuilderFactory.get("databaseToCsvFileStep").<Player, Player>chunk(10)
//				.reader(databaseCsvItemReader).writer(csvFileItemWriter).build();
//	}
//
//	@Bean
//	public JobCompletionNotificationListener jobCompletionNotificationListener() {
//		return new JobCompletionNotificationListener();
//	}
//
//	@Bean
//	public Job databaseToCsvFileJob(JobCompletionNotificationListener listener,
//			Step databaseToCsvFileStep) {
//		return jobBuilderFactory.get("databaseToCsvFileJob").incrementer(new RunIdIncrementer())
//				.listener(listener).flow(databaseToCsvFileStep).end().build();
//	}
//
//	public class JobCompletionNotificationListener extends JobExecutionListenerSupport {
//		@Override
//		public void afterJob(JobExecution jobExecution) {
//			if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
//				System.out.println("DATABASE TO CSV JOB FINISHED SUCCESSFULLY");
//			}
//		}
//	}
//
//}
