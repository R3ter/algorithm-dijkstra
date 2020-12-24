package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Optional;

class Road{
    City to;
    City from;
    Float distance;
    Road(City to,Float distance){

        this.distance=distance;
        this.to=to;
    }
}
class CityName{
    float x;
    float y;
    String name;
    CityName(float x,float y,String name){
        this.name=name;
        this.x=x;
        this.y=y;
    }
}
class City{
    int id;
    float x;
    float y;
    String name;
    ArrayList<Road> Roads;
    public void AddRoad(Road road){
        Roads.add(road);
    }
    City(float x,float y,String name,int id){
        this.x=x;
        this.y=y;
        this.id=id;
        this.name=name;
        Roads=new ArrayList<Road>();
    }
}
public class Main extends Application {
    City clickedCity;
    Scene scene;
    City[] cities;
    TextArea textArea;
    ArrayList<CityName> newCitiesNames;
    boolean addCityButtonClicked=false;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        newCitiesNames=new ArrayList();
        primaryStage.setTitle("Dijkstra algorithm");
        cities=new City[]{
                new City(134,92,"Haifa",0),//1
                new City(148,75,"Acre",1),//2
                new City(202,68,"Safed",2),//3
                new City(205,101,"Tiberias",3),//4
                new City(200,135,"Beit She'an",4),//5
                new City(174,109,"Nazareth",5),//6
                new City(175,139,"Jenin",6),//7
                new City(126,142,"Hadera",7),//8
                new City(122,158,"Netanya",8),//9
                new City(142,158,"Tulkarm",9),//10
                new City(172,174,"Nablus",10),//11
                new City(119,220,"Lod",11),//12
                new City(163,247,"jerusalem",12),//13
                new City(109,195,"Tel Aviv-Yafo",13),//14
                new City(163,227,"Ramallah",14),//15
                new City(188,232,"Jericho",15),//16
                new City(139,284,"Hebron",16),//17
                new City(107,324,"Be'er Sheva",17),//18
                new City(69,282,"Gaza",18),//19
                new City(78,264,"Ashkelon",19),//20
                new City(51,304,"Khan yunus",20),//21
                new City(46,323,"Rafah",21),//21
                new City(119,394,"al-Naqab",22),//21
                new City(89,245,"Ashdod",23),//23
                new City(158,261,"Bethlehem",24),//23
                new City(126,573,"Eilat",25),//24
                new City(175,325,"Neve Zohar",26),//25
                new City(156,312,"Arad",27),//26
                new City(147,334,"Dimona",28),//27
                new City(138,184,"Qalqilya",29),//28
        };
        scene=new Scene(root, 850, 700);
        Canvas canvas =  (Canvas) ((ScrollPane)(scene.lookup("#canvas"))).getContent().lookup("#canvasChild");
        textArea=((TextArea) ((GridPane)(scene.lookup("#tools"))).lookup("#result"));

        Button button =(Button) ((GridPane)(scene.lookup("#tools"))).lookup("#button");
        Button clearButton =(Button) ((GridPane)(scene.lookup("#tools"))).lookup("#clear");
        Button addCityButton =(Button) ((GridPane)(scene.lookup("#tools"))).lookup("#addCity");
        Button clearCitiesButton =(Button) ((GridPane)(scene.lookup("#tools"))).lookup("#clearCities");

        ComboBox comboStart =(ComboBox) scene.lookup("#ComboBoxStart");
        ComboBox comboFinish =(ComboBox) scene.lookup("#ComboBoxFinish");

        setDropListData(comboStart,comboFinish);

        comboStart.getSelectionModel().selectFirst();
        comboFinish.getSelectionModel().selectLast();
        clearButton.setOnAction(e->{
            for (City city :cities){
                city.Roads.clear();
            }
            drawGraphics(cities,canvas);
            clickedCity=null;
        });
        clearCitiesButton.setOnAction(event->{
            cities=new City[0];
            setDropListData(comboStart,comboFinish);
            drawGraphics(cities,canvas);
            clickedCity=null;
        });
        button.setOnAction(e -> {
            int x=0;
            for(Object name:comboStart.getItems()){
                if(name.toString()==comboStart.getValue()){
                    break;
                }
                x++;
            }
            int y=0;
            for(Object name:comboFinish.getItems()){
                if(name.toString()==comboFinish.getValue()){
                    break;
                }
                y++;
            }

            drawGraphics(cities,canvas);
            ArrayList visitedCities=new ArrayList<CityDistance>();
            CityDistance[] cityDistances=new CityDistance[cities.length];
            findPath(y,x,cities,null,visitedCities,cityDistances);
        });

        addCityButton.setOnAction(e->{
            addCityButtonClicked=true;
            clickedCity=null;
        });
        canvas.setOnMouseClicked(event ->{
            if(addCityButtonClicked){
                City[] newCities=new City[cities.length+1];
                int x=0;
                for(City city:cities){
                    newCities[x]=city;
                    x++;
                }

                TextInputDialog dialog = new TextInputDialog("new city");
                dialog.setTitle("Text Input Dialog");
                dialog.setHeaderText("Please provide city's name:");
                dialog.setContentText("City's name:");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(s ->{
                    boolean nameExsists=false;
                    for(CityName name : newCitiesNames){
                        if(name.name.equals(s)){
                            setTextArea(true,"Error: you cant use " +
                                    "the same name");
                            nameExsists=true;
                        }
                    }
                    newCities[cities.length]
                        = new City((float) event.getX(), (float) event.getY(), s, cities.length );
                    if(!nameExsists){
                        newCitiesNames.add(new CityName((float)event.getX()-s.length()-10,(float)event.getY()-5,s));
                        cities=newCities;
                    }
                });


                drawGraphics(cities,canvas);
                setDropListData(comboStart,comboFinish);

                addCityButtonClicked=false;
            }else if(clickedCity==null){
                float distance=10;
                City closestCity=null;
                int x=0;
                int f=0;
                for(City city : cities){
                    if( Math.abs((event.getX() - city.x))+ Math.abs((event.getY() - city.y)) < distance ){
                        distance=(float)( Math.abs((event.getX() - city.x))+ Math.abs((event.getY() - city.y)));
                        closestCity=city;
                    }
                }
                if(closestCity==null) return;

                System.out.println(closestCity.name);
                System.out.println(closestCity.id);

                System.out.println(event.getX()+" || "+event.getY());

                clickedCity=closestCity;
            }else{
                float distance1 = 10;
                City closestCity1 = null;
                int x1 = 0;
                int f1 = 0;
                for (City city : cities) {
                    if (Math.abs((event.getX() - city.x)) + Math.abs((event.getY() - city.y)) < distance1) {
                        distance1 = (float) (Math.abs((event.getX() - city.x)) + Math.abs((event.getY() - city.y)));
                        closestCity1 = city;
                    }
                }
                if(closestCity1==null){
                    drawGraphics(cities,canvas);
                    clickedCity=null;
                    return;
                }
                TextInputDialog dialog = new TextInputDialog("100");
                dialog.setTitle("Text Input Dialog");
                dialog.setHeaderText("Distance");
                dialog.setContentText("Please provide the road distance:");

                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()){
                    float distance2;
                    try{
                        distance2=Float.parseFloat(result.get());
                    }catch (Exception e){
                        distance2=0;
                        System.out.println("error");
                    }
                    final float finalDistance=distance2;
                    System.out.println(clickedCity.name);
                    System.out.println(closestCity1.name);
                    addRoad(clickedCity,new Road(closestCity1,finalDistance));
                }
                drawGraphics(cities,canvas);
                clickedCity=null;
            }
        });

        canvas.setOnMouseMoved(moveEvent-> {
            if (clickedCity == null) {
                return;
            }
            drawGraphics(cities, canvas);
            canvas.getGraphicsContext2D().strokeLine(clickedCity.x, clickedCity.y, moveEvent.getX(), moveEvent.getY());
        });






        setRoads(cities);


        drawGraphics(cities,canvas);



        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void setTextArea(boolean error,String text){
        if(error)
            textArea.setStyle("-fx-text-fill: #FF0000;");
        else
            textArea.setStyle("-fx-text-fill: #000000;");

        textArea.setText(text);
    }
    private void setDropListData(ComboBox comboFinish,ComboBox comboStart){
        ObservableList<String>  data = FXCollections.observableArrayList();
        for (City city:cities){
            data.add(city.name);
        }
        comboFinish.setItems(data);
        comboStart.setItems(data);
    }
    private void drawGraphics(City[] cities,Canvas canvas){
        canvas.getGraphicsContext2D().clearRect(0,0,canvas.getWidth(),canvas.getHeight());
        canvas.getGraphicsContext2D().setFill(Color.BLUE);
        canvas.getGraphicsContext2D().setStroke(Color.BROWN);
        canvas.getGraphicsContext2D().setLineWidth(1);
        canvas.getGraphicsContext2D().drawImage(new Image("palestine.jpg"),-2,-2);
        for (City city:cities) {
            for (Road road: city.Roads) {
                canvas.getGraphicsContext2D().strokeLine(city.x, city.y, road.to.x, road.to.y);
            }
            canvas.getGraphicsContext2D().fillOval(city.x-5, city.y-5,10,10);
        }
        for (CityName name:newCitiesNames){
            canvas.getGraphicsContext2D().fillText(name.name,name.x,name.y);
        }
    }
    public void addRoad(City city, Road road){
        city.AddRoad(new Road(road.to, road.distance));
        road.to.AddRoad(new Road(city,road.distance));
    }
    public void setRoads(City[] cities){
        addRoad(cities[0],new Road(cities[1],50f));
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
        addRoad(cities[14],new Road(cities[11],200f));
        addRoad(cities[14],new Road(cities[12],220f));
        addRoad(cities[13],new Road(cities[11],200f));
        addRoad(cities[9],new Road(cities[8],70f));
        addRoad(cities[7],new Road(cities[8],50f));
        addRoad(cities[7],new Road(cities[0],200f));
        addRoad(cities[5],new Road(cities[1],90f));
        addRoad(cities[5],new Road(cities[0],100f));
        addRoad(cities[8],new Road(cities[13],150f));
//        addRoad(cities[11],new Road(cities[9],300f));
        addRoad(cities[5],new Road(cities[3],70f));
        addRoad(cities[16],new Road(cities[17],150f));
        addRoad(cities[22],new Road(cities[17],320f));
        addRoad(cities[21],new Road(cities[20],50f));
        addRoad(cities[20],new Road(cities[18],70f));
        addRoad(cities[18],new Road(cities[19],80f));
        addRoad(cities[23],new Road(cities[19],70f));
        addRoad(cities[17],new Road(cities[21],230f));
        addRoad(cities[16],new Road(cities[18],230f));
        addRoad(cities[12],new Road(cities[24],30f));
        addRoad(cities[16],new Road(cities[24],70f));
        addRoad(cities[12],new Road(cities[11],170f));
        addRoad(cities[22],new Road(cities[25],700f));
        addRoad(cities[16],new Road(cities[22],400f));
        addRoad(cities[9],new Road(cities[29],90f));
        addRoad(cities[10],new Road(cities[29],110f));
        addRoad(cities[11],new Road(cities[29],110f));


    }
    class CityDistance {
        City city;
        City[] passCities;
        public float distance;
        CityDistance(City city, float distance,City[] passCities){
            this.passCities=passCities;
            this.city=city;
            this.distance=distance;
        }
    }

    public void findPath(int from, int to, City[] cities, City currentPoint
            , ArrayList<City> visitedCities, CityDistance[] cityDistances){

        if(to==from){
            setTextArea(true,"Error: Starting point is the same as ending point");
            return;
        };

        if(currentPoint==null) {
            currentPoint=cities[from];
            cityDistances[cities[from].id]=new CityDistance(currentPoint,0,new City[0]);
        }

        visitedCities.add(currentPoint);

        for(Road road : currentPoint.Roads) {
            if(cityDistances[road.to.id]==null){

                City[] passedCity=new City[cityDistances[currentPoint.id].passCities.length+1];

                System.arraycopy(cityDistances[currentPoint.id].passCities, 0, passedCity, 0, cityDistances[currentPoint.id].passCities.length);
                passedCity[passedCity.length-1]=currentPoint;

                cityDistances[road.to.id]=new CityDistance(road.to,road.distance+cityDistances[currentPoint.id].distance
                        ,passedCity);
            }
            if(cityDistances[road.to.id].distance>road.distance+cityDistances[currentPoint.id].distance){

                City[] passedCity=new City[cityDistances[currentPoint.id].passCities.length+1];

                System.arraycopy(cityDistances[currentPoint.id].passCities, 0, passedCity, 0, cityDistances[currentPoint.id].passCities.length);
                passedCity[passedCity.length-1]=currentPoint;
                cityDistances[road.to.id]=new CityDistance(road.to,road.distance+cityDistances[currentPoint.id].distance,
                        passedCity);

            }
        }
        City closestCity=null;
        float closestDistance=100000000;
        for(CityDistance city :cityDistances){
            if(city==null) continue;

            if(city.distance<closestDistance&&
            !visitedCities.contains(city.city)){
                closestDistance=city.distance;
                closestCity=city.city;
            }
        }
        if(cities[to]==currentPoint){
            Canvas canvas =  (Canvas) ((ScrollPane)(scene.lookup("#canvas")))
                    .getContent().lookup("#canvasChild");
            canvas.getGraphicsContext2D().setStroke(Color.GREEN);
            canvas.getGraphicsContext2D().setLineWidth(5);

            int x=0;
            for(City city :cityDistances[currentPoint.id].passCities){
                x++;
                if(cityDistances[currentPoint.id].passCities.length>x) {
                    canvas.getGraphicsContext2D().strokeLine(city.x, city.y,
                            cityDistances[currentPoint.id].passCities[x].x,
                            cityDistances[currentPoint.id].passCities[x].y);
                }

            }

            canvas.getGraphicsContext2D().strokeLine(cityDistances[currentPoint.id]
                            .passCities[cityDistances[currentPoint.id]
                            .passCities.length-1].x,
                    cityDistances[currentPoint.id]
                            .passCities[cityDistances[currentPoint.id]
                            .passCities.length-1].y,
                    cities[to].x,
                    cities[to].y);

            System.out.println("*************************");

            String VistedCities=cities[to].name;

            for(int i=cityDistances[currentPoint.id].passCities.length-1; i>=0; i--){

                VistedCities=VistedCities+" -> "+cityDistances[currentPoint.id].passCities[i].name;

//                System.out.println(city.name);
//                System.out.println(city.id);
//                System.out.println(cityDistances[city.id].distance);
            }
            setTextArea(false,VistedCities+"\n\n\n\n" +
                    "dictance="+cityDistances[to].distance);
            System.out.println(cityDistances[to].distance);
            System.out.println("*************************");
            return;

        }
        if(closestCity==null) {
            setTextArea(true,"Error: no path was found");
            return;
        }
        findPath(from,to,cities,closestCity,visitedCities,cityDistances);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
