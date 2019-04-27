package gfx.DataModels;

public class MaterialName {
	/** Material Name in Material Library. */
	public String name;
	/** Index of line in which material change . */
	public int lineNumber;

	MaterialName() {

	}

	public MaterialName(int lineNumber, String name) {
		this.lineNumber = lineNumber;
		this.name = name;
	}
}
