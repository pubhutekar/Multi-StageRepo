package deque.com;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public class MultiStageProcessor {
	private final StageQueue[] stages;
	private final ExecutorService executor;

	public MultiStageProcessor(int numberOfStages, int threadPoolSize) {
		this.stages = new StageQueue[numberOfStages];
		this.executor = Executors.newFixedThreadPool(threadPoolSize);
		for (int i = 0; i < numberOfStages; i++) {
			this.stages[i] = new BlockingStageQueue(); // Example using BlockingStageQueue, can be replaced with other
														// implementations
		}
	}

	public void processItem(Item item) {
		stages[item.getStage()].addItem(item);
		executor.submit(() -> processStage(item.getStage()));
	}

	private void processStage(int stage) {
		try {
			Item item = stages[stage].getItem();
			System.out.println("Processing item: " + item.getDescription() + " at stage: " + stage);
			// Simulate processing time
			Thread.sleep(1000);
			if (stage + 1 < stages.length) {
				stages[stage + 1]
						.addItem(new Item(item.getItemId(), stage + 1, item.getPriority(), item.getDescription()));
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	public void shutdown() {
		executor.shutdown();
		try {
			if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
				executor.shutdownNow();
			}
		} catch (InterruptedException e) {
			executor.shutdownNow();
		}
	}

	public static void main(String[] args) {
		MultiStageProcessor processor = new MultiStageProcessor(3, 4); // 3 stages, 4 threads

		processor.processItem(new Item(1, 0, 2, "Item 1"));
		processor.processItem(new Item(2, 0, 1, "Item 2"));
		processor.processItem(new Item(3, 0, 3, "Item 3"));

		processor.shutdown();
	}
}
