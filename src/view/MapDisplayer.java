package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class MapDisplayer extends Canvas
{
	//----------------------------------------------------Parameters----------------------------------------------------
    int[][] mapData;
    double min=Double.MAX_VALUE;
    double max=0;
    
	//----------------------------------------------------Functions----------------------------------------------------

    //set map data:
    public void setMapData(int[][] mapData)
    {
        this.mapData = mapData;

        for(int i=0;i<mapData.length;i++)
            for (int j=0;j<mapData[i].length;j++)
            {
                if(min>mapData[i][j])
                    min=mapData[i][j];
                if(max<mapData[i][j])
                    max=mapData[i][j];
            }
        
        double newMax=255;
        double newMin=0;
        for (int i=0;i<mapData.length;i++)
            for (int j=0;j<mapData[i].length;j++)
                mapData[i][j]=(int)((mapData[i][j]-min)/(max-min)*(newMax-newMin)+newMin);

        redraw();
    }

    //redraw the map:
    public void redraw()
    {
        if(mapData!=null)
        {
            double H=getHeight();
            double W=getWidth();
            double h=H/mapData.length;
            double w=W/mapData[0].length;
            GraphicsContext gc=getGraphicsContext2D();

            for (int i=0;i<mapData.length;i++)
                for (int j=0;j<mapData[i].length;j++)
                {
                    int temp=mapData[i][j];
                    gc.setFill(Color.rgb(255-temp,0+temp,0));
                    gc.fillRect(j*w,i*h,w,h);
                }
        }
    }
}
