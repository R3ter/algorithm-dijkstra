package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
class Road{
    City to;
    Float distance;
    Road(City to,Float distance){
        this.distance=distance;
        this.to=to;
    }
}
class City{
    float x;
    float y;
    String name;
    ArrayList<Road> Roads;
    public void AddRoad(Road road){
        Roads.add(road);
    }
    City(float x,float y,String name){
        this.x=x;
        this.y=y;
        this.name=name;
        Roads=new ArrayList<Road>();
    }
}
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");

        City[] cities={
                new City(134,92,"Haifa"),//1
                new City(148,75,"Acre"),//2
                new City(202,68,"Safed"),//3
                new City(205,101,"Tiberias"),//4
                new City(200,135,"Beit She'an"),//5
                new City(174,109,"Nazareth"),//6
                new City(175,139,"Jenin"),//7
                new City(126,142,"Hadera"),//8
                new City(122,158,"Netanya"),//9
                new City(142,158,"Tulkarm"),//10
                new City(172,174,"Nablus"),//11
                new City(119,220,"Lod"),//12
                new City(163,247,"jerusalem"),//13
                new City(109,195,"Tel Aviv-Yafo"),//14
                new City(163,227,"Ramallah"),//15
                new City(188,232,"Jericho"),//16
                new City(139,284,"Hebron"),//17
        };
        setRoads(cities);
        Scene scene=new Scene(root, 850, 700);
        Canvas canvas = (Canvas) scene.lookup("#canvas");

        canvas.setOnMouseClicked(event ->{
//            System.out.println(event.getX()+" || "+event.getY());
            float distance=100000;
            City closestCity=null;
            int x=0;
            int f=0;
            for(City city : cities){
                x++;
                if( Math.abs((event.getX() - city.x))+ Math.abs((event.getY() - city.y)) < distance ){
                    distance=(float)( Math.abs((event.getX() - city.x))+ Math.abs((event.getY() - city.y)));
                    closestCity=city;
                    f=x;
                }
            }
            System.out.println(closestCity.name);
            System.out.println(f-1);
        });
        canvas.getGraphicsContext2D().drawImage(new Image("palestine.jpg"),-2,-2);

        canvas.getGraphicsContext2D().setFill(Color.BLUE);
        canvas.getGraphicsContext2D().setStroke(Color.GREEN);
        canvas.getGraphicsContext2D().setLineWidth(3);
        for (City city:cities) {
            canvas.getGraphicsContext2D().fillOval(city.x-5, city.y-5,10,10);

            for (Road road: city.Roads) {
                canvas.getGraphicsContext2D().strokeLine(city.x, city.y, road.to.x, road.to.y);

            }
        }

//        canvas.setScaleX(2);
//        canvas.setScaleY(2);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public void addRoad(City city, Road road){
        city.AddRoad(new Road(road.to, road.distance));
        road.to.AddRoad(new Road(city,road.distance));
    }
    public void setRoads(City[] cities){
        addRoad(cities[0],new Road(cities[1],25f));
        addRoad(cities[1],new Road(cities[2],100f));
        addRoad(cities[2],new Road(cities[3],50f));
        addRoad(cities[3],new Road(cities[4],100f));
        addRoad(cities[4],new Road(cities[6],100f));
        addRoad(cities[6],new Road(cities[5],100f));
        addRoad(cities[10],new Road(cities[6],150f));
        addRoad(cities[10],new Road(cities[9],150f));
        addRoad(cities[14],new Road(cities[10],250f));
        addRoad(cities[14],new Road(cities[15],100f));
        addRoad(cities[14],new Road(cities[12],70f));
        addRoad(cities[16],new Road(cities[12],200f));
        addRoad(cities[14],new Road(cities[11],200f));
        addRoad(cities[14],new Road(cities[12],220f));
        addRoad(cities[13],new Road(cities[11],200f));
        addRoad(cities[9],new Road(cities[8],70f));
        addRoad(cities[7],new Road(cities[8],50f));
        addRoad(cities[7],new Road(cities[0],200f));
        addRoad(cities[5],new Road(cities[1],90f));
        addRoad(cities[5],new Road(cities[0],100f));
        addRoad(cities[8],new Road(cities[13],150f));
        addRoad(cities[11],new Road(cities[9],300f));
        addRoad(cities[5],new Road(cities[3],70f));

        /*
                new City(134,92,"Haifa"),//1
                new City(148,75,"Acre"),//2
                new City(202,68,"Safed"),//3
                new City(205,101,"Tiberias"),//4
                new City(200,135,"Beit She'an"),//5
                new City(174,109,"Nazareth"),//6
                new City(175,139,"Jenin"),//7
                new City(126,142,"Hadera"),//8
                new City(122,158,"Netanya"),//9
                new City(142,158,"Tulkarm"),//10
                new City(172,174,"Nablus"),//11
                new City(119,220,"Lod"),//12
                new City(163,247,"jerusalem"),//13
                new City(109,195,"Tel Aviv-Yafo"),//14
                new City(163,227,"Ramallah"),//15
                new City(188,232,"Jericho"),//16
                new City(139,284,"Hebron"),//17
         */
        findPath(cities[0],cities[1]);
    }
    public ArrayList<Road> findPath(City from,City to){
        City currentPoint=from;
        Road shortestRoad=from.Roads.get(0);
        for(Road road : from.Roads){
            if(road.distance<shortestRoad.distance)
                shortestRoad=road;
            if(road.to==to){
                System.out.println(to.name);
                return new ArrayList<>() ;
            }
        }
        findPath(from,to);
        return new ArrayList<>();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
