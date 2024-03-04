package com.sample.project.config;

import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sample.project.dto.MovedPlayerRepository;
import com.sample.project.dto.PlayerRepository;
import com.sample.project.entity.MovedPlayer;
import com.sample.project.entity.Player;

@Configuration
@EnableBatchProcessing
public class MovedPlayer2Config {

	LocalDateTime currentDate = LocalDateTime.now();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HHmm");
	String formattedDate = currentDate.format(formatter);

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	public PlayerRepository playerRepository;

	@Autowired
	public MovedPlayerRepository movedPlayerRepository;

	@Bean
	public JdbcCursorItemReader<Player> databaseCsvItemReader(DataSource dataSource) {
		JdbcCursorItemReader<Player> databaseReader = new JdbcCursorItemReader<>();
		databaseReader.setDataSource(dataSource);
		databaseReader.setSql(
				"SELECT name, age, gender, mail, wage, boss, department, receivedDate, departmentCode FROM player");
		databaseReader.setRowMapper(new BeanPropertyRowMapper<>(Player.class));
		return databaseReader;
	}

	@Bean
	public ItemProcessor<Player, MovedPlayer> playerMoveProcessor() {
		final Map<String, Player> lastSeenPlayerRecord = new HashMap<>();

		return new ItemProcessor<Player, MovedPlayer>() {
			@Override
			public MovedPlayer process(Player player) throws Exception {
				String latestReceivedDate = playerRepository.findNewDate();

				MovedPlayer movedPlayer = new MovedPlayer();
				movedPlayer.setName(player.getName());

				Player previousRecord = lastSeenPlayerRecord.get(player.getName());

				if (player.getReceivedDate().equals(latestReceivedDate)) {
					if (previousRecord == null) {
						// This is a new entry
						populateNewEntry(movedPlayer, player);
					} else {
						// Check what has changed
						boolean departmentChanged = !player.getDepartmentCode()
								.equals(previousRecord.getDepartmentCode());
						boolean mailChanged = !player.getMail().equals(previousRecord.getMail());
						boolean nameChanged = !player.getName().equals(previousRecord.getName());

						if (departmentChanged && !mailChanged && !nameChanged) {
							// Only department code has changed
							movedPlayer.setCategory("Code Changed");
						} else if (mailChanged || nameChanged) {
							// Mail or name has changed
							movedPlayer.setCategory("Update");
						} else {
							// No relevant changes to process
							return null;
						}

						populateUpdateEntry(movedPlayer, player, previousRecord);
					}

					lastSeenPlayerRecord.put(player.getName(), player);
					return movedPlayer;
				} else {
					// Record is not for the latest date; update last seen but do not process
					lastSeenPlayerRecord.put(player.getName(), player);
					return null;
				}
			}

			private void populateNewEntry(MovedPlayer movedPlayer, Player player) {
				movedPlayer.setCategory("New Entry");
				movedPlayer.setNewDepartment(player.getDepartment());
				movedPlayer.setNewDepartmentCode(player.getDepartmentCode());
				movedPlayer.setMail(player.getMail());
				movedPlayer.setDate(formattedDate);
			}

			private void populateUpdateEntry(MovedPlayer movedPlayer, Player player,
					Player previousRecord) {
				movedPlayer.setNewDepartment(player.getDepartment());
				movedPlayer.setNewDepartmentCode(player.getDepartmentCode());
				movedPlayer.setOldDepartment(previousRecord.getDepartment());
				movedPlayer.setOldDepartmentCode(previousRecord.getDepartmentCode());
				movedPlayer.setMail(player.getMail());
				movedPlayer.setDate(formattedDate);
			}
		};
	}

	@Bean
	public Tasklet deleteOldMovedPlayerRecordsTasklet(DataSource dataSource) {
		return (contribution, chunkContext) -> {
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			String sql = "DELETE FROM MOVEDPLAYER WHERE DATEDIFF('DAY', PARSEDATETIME(SUBSTRING(date, 1, 8), 'yyyyMMdd'), CURRENT_DATE()) > 90";
			int rowsAffected = jdbcTemplate.update(sql);
			System.out.println("Deleted " + rowsAffected + " old records older than three months.");
			return RepeatStatus.FINISHED;
		};
	}

	@Bean
	public FlatFileItemWriter<MovedPlayer> MovedPlayerWriterItemWriter() {
		FlatFileItemWriter<MovedPlayer> writer = new FlatFileItemWriter<>();
		writer.setResource(new FileSystemResource("src/main/resources/MovedPlayer.csv"));
		writer.setHeaderCallback(new FlatFileHeaderCallback() {
			@Override
			public void writeHeader(Writer writer) throws IOException {
				writer.write(
						"CATEGORY,NAME,OLDDEPARTMENT,OLDDEPARTMENTCODE,NEWDEPARTMENT,NEWDEPARTMENTCODE");
			}
		});
		writer.setLineAggregator(new DelimitedLineAggregator<MovedPlayer>() {
			{
				setDelimiter(",");
				setFieldExtractor(new BeanWrapperFieldExtractor<MovedPlayer>() {
					{
						setNames(new String[] { "category", "name", "oldDepartment",
								"oldDepartmentCode", "newDepartment", "newDepartmentCode" });
					}
				});
			}
		});

		return writer;
	}

	@Bean
	public RepositoryItemWriter<MovedPlayer> MovedPlayerWriter() {
		RepositoryItemWriter<MovedPlayer> writer = new RepositoryItemWriter<>();
		writer.setRepository(movedPlayerRepository);
		writer.setMethodName("save");
		return writer;
	}

	@Bean
	public CompositeItemWriter<MovedPlayer> MovedPlayerCompositeItemWriter(
			RepositoryItemWriter<MovedPlayer> MovedPlayerWriter,
			FlatFileItemWriter<MovedPlayer> MovedPlayerWriterItemWriter) {
		CompositeItemWriter<MovedPlayer> writer = new CompositeItemWriter<>();
		writer.setDelegates(Arrays.asList(MovedPlayerWriter, MovedPlayerWriterItemWriter));
		return writer;
	}

	@Bean
	public Step csvFileStep(StepBuilderFactory stepBuilderFactory, ItemReader<Player> reader,
			ItemProcessor<Player, MovedPlayer> processor,
			CompositeItemWriter<MovedPlayer> MovedPlayerCompositeItemWriter) {
		return stepBuilderFactory.get("csvFileStep").<Player, MovedPlayer>chunk(10).reader(reader)
				.processor(processor).writer(MovedPlayerCompositeItemWriter).build();
	}

	@Bean
	public Step deleteOldRecordsStep(StepBuilderFactory stepBuilderFactory, DataSource dataSource) {
		return stepBuilderFactory.get("deleteOldRecordsStep")
				.tasklet(deleteOldMovedPlayerRecordsTasklet(dataSource)).build();
	}

	@Bean
	public Job exportPlayerJob(JobBuilderFactory jobBuilderFactory, Step deleteOldRecordsStep,
			Step csvFileStep) {
		return jobBuilderFactory.get("exportPlayerJob").incrementer(new RunIdIncrementer())
				.start(deleteOldRecordsStep) // Cleanup step runs first
				.next(csvFileStep) // Then proceed with the main processing step
				.build(); // Finalize the job configuration
	}

}
