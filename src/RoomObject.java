/**
 * 
 */

/**
 * @author Kee
 *
 */

/*name,
type,
description
total,
delete?,*/
									
public class RoomObject {
		private String name;
		private String type;
		private String description;
		private double total;

	public RoomObject() {
		name = "";
		type = "";
		description = "";
		total = 0;
	}
	
	public RoomObject( String inName, String inType, String inDesc, double inTotal ){
		name = inName;
		type = inType;
		description = inDesc;
		total = inTotal;	
	}
	
	public String getName(){
		return name;		
	}
	
	public String getType(){
		return type;
	}
	
	public String getDesc(){
		return description;
	}
	
	public double getTotal(){
		return total;
	}
}
