package com.wael.janlandonlam;

import android.app.*;import android.os.*;import android.view.*;import android.graphics.*;import android.content.*;import java.util.*;

public class MainActivity extends Activity{
 public void onCreate(Bundle b){super.onCreate(b);getWindow().setFlags(1024,1024);setContentView(new Game(this));}
}
class Game extends View{
 Paint p=new Paint(1); Random r=new Random(); float y=700,vy=0,scroll=0; boolean press=false,dead=false; int score=0,best=0; ArrayList<Gap> gaps=new ArrayList<>();
 Game(Context c){super(c);p.setTypeface(Typeface.create("sans",Typeface.BOLD)); for(int i=0;i<12;i++)gaps.add(new Gap(500+i*300,360+r.nextInt(500)));}
 protected void onDraw(Canvas c){int w=getWidth(),h=getHeight();c.drawColor(Color.rgb(5,7,20));
  p.setStrokeWidth(3);p.setColor(Color.rgb(20,40,70));for(int x=0;x<w;x+=80)c.drawLine(x,0,x,h,p);for(int yy=0;yy<h;yy+=80)c.drawLine(0,yy,w,yy,p);
  if(!dead){vy+=(press?-0.82f:0.82f);vy=Math.max(-9,Math.min(9,vy));y+=vy;scroll+=6.4f+Math.min(score/220f,2.5f);score=(int)(scroll/12);if(y<45||y>h-45)dead=true;}
  float corridor=Math.max(290,480-score/28f); for(Gap g:gaps){float x=g.x-scroll; if(x<-100){g.x+=gaps.size()*300;g.cy=320+r.nextInt(Math.max(300,h-640));} x=g.x-scroll; if(x>-120&&x<w+120){float top=g.cy-corridor/2,bottom=g.cy+corridor/2;p.setColor(Color.rgb(255,35,140));c.drawRect(x-38,0,x+38,top,p);p.setColor(Color.rgb(0,220,255));c.drawRect(x-38,bottom,x+38,h,p);for(int k=0;k<3;k++){p.setColor(Color.WHITE);Path t=new Path();t.moveTo(x-38+k*30,top);t.lineTo(x-25+k*30,top+22);t.lineTo(x-12+k*30,top);t.close();c.drawPath(t,p);Path b=new Path();b.moveTo(x-38+k*30,bottom);b.lineTo(x-25+k*30,bottom-22);b.lineTo(x-12+k*30,bottom);b.close();c.drawPath(b,p);} if(Math.abs(x-w*.25f)<48&&(y<top+24||y>bottom-24))dead=true;}}
  float px=w*.25f;p.setColor(Color.rgb(0,240,255));Path hero=new Path();hero.moveTo(px,y-25);hero.lineTo(px+31,y);hero.lineTo(px,y+25);hero.lineTo(px-31,y);hero.close();c.drawPath(hero,p);p.setColor(Color.WHITE);c.drawCircle(px-8,y-5,5,p);c.drawCircle(px+9,y-5,5,p);
  p.setTextAlign(Paint.Align.CENTER);p.setTextSize(42);p.setColor(Color.WHITE);c.drawText("JANA London 1234",w/2,70,p);p.setTextSize(30);c.drawText("SCORE  "+score,w/2,112,p);
  if(dead){best=Math.max(best,score);p.setColor(0xCC080A18);c.drawRect(35,h/2-170,w-35,h/2+170,p);p.setColor(Color.WHITE);p.setTextSize(55);c.drawText("CRASH!",w/2,h/2-70,p);p.setTextSize(30);c.drawText("Score "+score+"   •   Best "+best,w/2,h/2,p);p.setTextSize(26);c.drawText("TAP TO TRY AGAIN",w/2,h/2+85,p);}else{p.setTextSize(22);p.setColor(Color.LTGRAY);c.drawText("HOLD = UP   •   RELEASE = DOWN",w/2,h-35,p);}invalidate(); }
 public boolean onTouchEvent(MotionEvent e){if(e.getAction()==0){if(dead){dead=false;y=getHeight()/2;vy=0;scroll=0;score=0;for(int i=0;i<gaps.size();i++){gaps.get(i).x=500+i*300;gaps.get(i).cy=360+r.nextInt(Math.max(300,getHeight()-720));}}press=true;}if(e.getAction()==1||e.getAction()==3)press=false;return true;}
 static class Gap{float x,cy;Gap(float a,float b){x=a;cy=b;}}
}
