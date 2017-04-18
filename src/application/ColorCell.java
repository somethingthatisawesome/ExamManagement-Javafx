package application;

import ell.Gobal;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class ColorCell extends ListCell<String> {

	@Override
	protected void updateItem(String item, boolean empty) {
		// TODO Auto-generated method stub
		super.updateItem(item, empty);
		
		if(empty)
		{
			setText(null);
			setGraphic(null);
		}
		else
		{
			setText(item);
			Shape shape = this.getColor(item);
			setGraphic(shape);
		}
	}

	public Shape getColor(String color)
	{
		Shape shape = null;
		shape = new Rectangle(0, 0, 20, 20);
		switch(color.toLowerCase())
		{
		case "pink":
			shape.setFill(Color.PINK);
			break;
		case "violet":
			shape.setFill(Color.VIOLET);
			break;
		case "bright green":
			shape.setFill(Color.LAWNGREEN);
			break;
		case "dark yellow":
			shape.setFill(Color.DARKGOLDENROD);
			break;
		case "teal":
			shape.setFill(Color.TEAL);
			break;
		case "grey-50%":
			shape.setFill(Color.GREY);
			break;
		case "grey-25%":
			shape.setFill(Color.LIGHTGREY);
			break;
		case "turquoise":
			shape.setFill(Color.TURQUOISE);
			break;
		case "black":
			shape.setFill(Gobal._BLACK);
			break;
		case "dark red":
			shape.setFill(Gobal._DARKRED);
			break;
		case "red":
			shape.setFill(Gobal._RED);
			break;
		case "yellow":
			shape.setFill(Gobal._YELLOW);
			break;
		case "orange":
			shape.setFill(Gobal._ORANGE);
			break;
		case "light green":
			shape.setFill(Gobal._LIGHTGREEN);
			break;
		case "green":
			shape.setFill(Gobal._GREEN);
			break;
		case "light blue":
			shape.setFill(Gobal._LIGHTBLUE);
			break;
		case "blue":
			shape.setFill(Gobal._BLUE);
			break;
		case "dark blue":
			shape.setFill(Gobal._DARKBLUE);
			break;
		case "purple":
			shape.setFill(Gobal._PURPLE);
			break;
		default:
			shape.setFill(Color.WHITE);
			break;
		}
		return shape;
	}
}
