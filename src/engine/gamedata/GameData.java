package engine.gamedata;

import java.util.ArrayList;

public class GameData {

	private ArrayList<SaveData> savedData;

	public GameData(){
		this.savedData = new ArrayList<SaveData>();
	}

	public void saveData(SaveData data){
		if(this.getData(data.getIdentifier()) != null){
			this.removeData(data);
			System.err.println("Class 'Player' : '" + data.getIdentifier() + "' overwritten!");
		}


		this.savedData.add(data);

	}

	private void removeData(SaveData data){
		SaveData saveDataFound = null;
		for (SaveData saveData : savedData) {
			if(saveData.getIdentifier() == data.getIdentifier()){
				saveDataFound = saveData;
			}
		}
		this.savedData.remove(saveDataFound);
	}

	public SaveData getData(String identifier){
		if(this.savedData == null){
			return null;
		}
		for (SaveData saveData : this.savedData) {
			if(saveData.getIdentifier() == identifier){
				return saveData;
			}
		}
		return null;
	}
}
