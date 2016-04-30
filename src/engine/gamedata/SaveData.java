package engine.gamedata;

public class SaveData {
	private String identifier;
	private String data;
	private int type;

	private final static int TYPE_STRING = 0;
	private final static int TYPE_INTEGER = 1;
	private final static int TYPE_BOOLEAN = 2;


	public SaveData(String identifier, String data){
		this.identifier = identifier;
		this.data = data;
		this.type = TYPE_STRING;
	}

	public String getIdentifier(){
		return this.identifier;
	}

	public SaveData(String identifier, int data){
		this.identifier = identifier;
		this.data = String.valueOf(data);
		this.type = TYPE_INTEGER;
	}

	public SaveData(String identifier, boolean data){
		this.identifier = identifier;
		this.data = String.valueOf(data);
		this.type = TYPE_BOOLEAN;
	}

	public String getString(){
		if(type == TYPE_STRING){
			return this.data;
		}else{
			System.err.println("Class 'SaveData' : '" + this.getIdentifier() + "' is not a String!");
			return null;
		}
	}

	public Boolean getBoolean(){
		if(type == TYPE_BOOLEAN){
			return Boolean.valueOf(this.data);
		}else{
			System.err.println("Class 'SaveData' : '" + this.getIdentifier() + "' is not a Boolean!");
			return null;
		}
	}

	public int getInteger(){
		if(this.type == TYPE_INTEGER){
			return Integer.parseInt(this.data);
		}else{
			System.err.println("Class 'SaveData' : '" + this.getIdentifier() + "' is not an Integer!");
			return 0;
		}
	}




}
