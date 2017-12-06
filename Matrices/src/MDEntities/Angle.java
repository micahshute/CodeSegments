package MDEntities;

import java.util.Optional;

public class Angle {

    private double degree;
    private boolean degreeMode;

    public Angle(double degree) throws IllegalArgumentException{
        this(degree, true);

    }

    public Angle(double degree, boolean degreeMode) throws IllegalArgumentException{

        this.degreeMode = degreeMode;

        if(degree < -360.0){
            throw new IllegalArgumentException();
        }
        if(degree > 360){
            degree = degree % 360;
        }
        if(degree > 0){
            this.degree = degree;
        }else{
            this.degree = 360.0 + degree;
        }

    }


    public Angle(boolean degreeMode){
        this(0,degreeMode);
    }

    public Angle(){
        this(0,true);
    }


    public double getDegree(){
        return this.degree;
    }

    public double getRadian(){
        return degree * 2 * Math.PI / 360.0;
    }

    public boolean isDegreeMode(){
        return degreeMode;
    }

    public double getAngle(){
        if(degreeMode){
            return getDegree();
        }else{
            return getRadian();
        }
    }

    public double cos(){
        return Math.cos(this.getRadian());
    }

    public double sin(){
        return Math.sin(this.getRadian());
    }

    public Optional<Double> tan(){
        if(degree == 90 || degree == 180 || degree == 270){
            return Optional.empty();
        }
        return Optional.of(Math.tan(this.getRadian()));
    }


    //MARK: Setters

    public void setDegreeMode(boolean to){
        this.degreeMode = to;
    }

    public void setDegree(double to){
        if(to < -360.0){
            return;
        }

        if(degree > 0){
            to = to % 360;
            this.degree = to;
        }else{
            this.degree = 360.0 + to;
        }
    }

    public void setRadian(double radian){
        setDegree((radian * 360) / (2 * Math.PI));
    }

    public void setAngle(double angle){
        if(degreeMode){
            setDegree(angle);
        }else{
            setRadian(angle);
        }
    }

}
