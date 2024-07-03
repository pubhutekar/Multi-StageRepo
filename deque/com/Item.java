package deque.com;

//public class aa {
	public class Item implements Comparable<Item> {
	    private final int itemId;
	    private final int stage;
	    private final int priority;
	    private final String description;

	    public Item(int itemId, int stage, int priority, String description) {
	        this.itemId = itemId;
	        this.stage = stage;
	        this.priority = priority;
	        this.description = description;
	    }

	    public int getItemId() {
	        return itemId;
	    }

	    public int getStage() {
	        return stage;
	    }

	    public int getPriority() {
	        return priority;
	    }

	    public String getDescription() {
	        return description;
	    }

	    @Override
	    public int compareTo(Item other) {
	        return Integer.compare(other.priority, this.priority); // Higher priority first
	    }
	}



