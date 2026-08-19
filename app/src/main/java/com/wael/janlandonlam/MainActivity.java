package com.wael.janlandonlam;

import android.app.*;import android.os.*;import android.view.*;import android.graphics.*;import android.content.*;import java.util.*;

public class MainActivity extends Activity{
 @Override public void onCreate(Bundle b){super.onCreate(b);getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);setContentView(new Game(this));}
}

class Game extends View{
 Paint p=new Paint(1); Random r=new Random(); ArrayList<Gap> gaps=new ArrayList<>();
 int screen=0,level=1,score=0,best=0; float y,vy,scroll; boolean press=false,dead=false;
 Game(Context c){super(c);p.setTypeface(Typeface.create("sans",Typeface.BOLD));setBackgroundColor(Color.BLACK);}
 void startLevel(int l){level=l;score=0;scroll=0;vy=0;dead=false;y=getHeight()>0?getHeight()/2f:700;gaps.clear();float spacing=level==1?390:level==2?350:level==3?320:290;for(int i=0;i<14;i++)gaps.add(new Gap(700+i*spacing,360+r.nextInt(420)));screen=1;invalidate();}
 @Override protected void onDraw(Canvas c){super.onDraw(c);int w=getWidth(),h=getHeight();c.drawColor(Color.rgb(6,8,22));drawGrid(c,w,h);if(screen==0){drawMenu(c,w,h);return;}drawGame(c,w,h);invalidate();}
 void drawGrid(Canvas c,int w,int h){p.setStrokeWidth(2);p.setColor(Color.rgb(18,35,60));for(int x=0;x<w;x+=70)c.drawLine(x,0,x,h,p);for(int yy=0;yy<h;yy+=70)c.drawLine(0,yy,w,yy,p);}
 void drawMenu(Canvas c,int w,int h){p.setTextAlign(Paint.Align.CENTER);p.setColor(Color.WHITE);p.setTextSize(54);c.drawText("JANA",w/2,120,p);p.setTextSize(34);p.setColor(Color.CYAN);c.drawText("1234 Levels",w/2,165,p);p.setTextSize(24);p.setColor(Color.LTGRAY);c.drawText("Choose a level",w/2,230,p);float top=310;for(int i=1;i<=4;i++){float cy=top+(i-1)*150;p.setColor(i==1?0xFF18D8FF:i==2?0xFF7C4DFF:i==3?0xFFFF4081:0xFFFFA000);c.drawRoundRect(w*.18f,cy-48,w*.82f,cy+48,26,26,p);p.setColor(Color.WHITE);p.setTextSize(32);c.drawText("LEVEL "+i,w/2,cy+11,p);}p.setTextSize(20);p.setColor(Color.GRAY);c.drawText("Hold = up   •   Release = down",w/2,h-50,p);}
 void drawGame(Canvas c,int w,int h){float gravity=.58f+.06f*level,maxV=6.5f+.55f*level,speed=4.8f+.85f*level;float corridor=level==1?580:level==2?510:level==3?450:400;float spacing=level==1?390:level==2?350:level==3?320:290;
  if(!dead){vy+=(press?-gravity:gravity);vy=Math.max(-maxV,Math.min(maxV,vy));y+=vy;scroll+=speed;score=(int)(scroll/10);if(y<35||y>h-35)dead=true;}
  for(Gap g:gaps){float x=g.x-scroll;if(x<-120){g.x+=gaps.size()*spacing;g.cy=300+r.nextInt(Math.max(300,h-600));}x=g.x-scroll;if(x>-140&&x<w+140){float top=g.cy-corridor/2,bottom=g.cy+corridor/2;p.setColor(0xFFFF2A93);c.drawRect(x-32,0,x+32,top,p);p.setColor(0xFF00D8FF);c.drawRect(x-32,bottom,x+32,h,p);drawSpikes(c,x,top,bottom);if(Math.abs(x-w*.25f)<42&&(y<top+20||y>bottom-20))dead=true;}}
  float px=w*.25f;drawHero(c,px,y);p.setTextAlign(Paint.Align.CENTER);p.setColor(Color.WHITE);p.setTextSize(32);c.drawText("JANA 1234 Levels",w/2,55,p);p.setTextSize(24);c.drawText("LEVEL "+level+"   •   SCORE "+score,w/2,92,p);
  if(dead){best=Math.max(best,score);p.setColor(0xD00A0C19);c.drawRoundRect(35,h/2-175,w-35,h/2+175,30,30,p);p.setColor(Color.WHITE);p.setTextSize(48);c.drawText("CRASH!",w/2,h/2-72,p);p.setTextSize(27);c.drawText("Score "+score+"   •   Best "+best,w/2,h/2-10,p);p.setTextSize(24);p.setColor(Color.CYAN);c.drawText("Tap to retry",w/2,h/2+65,p);p.setColor(Color.LTGRAY);c.drawText("Top-left = levels",w/2,h/2+112,p);}else{p.setTextSize(19);p.setColor(Color.LTGRAY);c.drawText("HOLD = UP   •   RELEASE = DOWN",w/2,h-28,p);} }
 void drawSpikes(Canvas c,float x,float top,float bottom){p.setColor(Color.WHITE);for(int k=0;k<3;k++){float sx=x-29+k*24;Path a=new Path();a.moveTo(sx,top);a.lineTo(sx+11,top+18);a.lineTo(sx+22,top);a.close();c.drawPath(a,p);Path b=new Path();b.moveTo(sx,bottom);b.lineTo(sx+11,bottom-18);b.lineTo(sx+22,bottom);b.close();c.drawPath(b,p);}}
 void drawHero(Canvas c,float x,float yy){p.setColor(0xFF23E8FF);Path hero=new Path();hero.moveTo(x,yy-20);hero.lineTo(x+28,yy);hero.lineTo(x,yy+20);hero.lineTo(x-28,yy);hero.close();c.drawPath(hero,p);p.setColor(Color.WHITE);c.drawCircle(x-7,yy-4,4,p);c.drawCircle(x+8,yy-4,4,p);}
 @Override public boolean onTouchEvent(MotionEvent e){float x=e.getX(),yy=e.getY();if(e.getAction()==MotionEvent.ACTION_DOWN){if(screen==0){float top=310;for(int i=1;i<=4;i++){float cy=top+(i-1)*150;if(yy>cy-60&&yy<cy+60){startLevel(i);return true;}}}else if(dead){if(x<getWidth()*.3f&&yy<getHeight()*.25f){screen=0;invalidate();return true;}startLevel(level);return true;}press=true;}else if(e.getAction()==MotionEvent.ACTION_UP||e.getAction()==MotionEvent.ACTION_CANCEL)press=false;return true;}
 static class Gap{float x,cy;Gap(float x,float cy){this.x=x;this.cy=cy;}}
}
