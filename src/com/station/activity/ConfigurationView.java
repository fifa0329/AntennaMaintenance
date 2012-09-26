package com.station.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.View;

public class ConfigurationView extends View {
	
	private Paint paint;
	private Paint font_Paint;
	// ��ֵ��ʾ��ƫ����
	private int numWidth = 9;
	
	// ��ʼ�߶�Ϊ ���߶ȼ�ȥ 80 ��ע������ĸ߶��Ƿ��ģ�Ҳ����˵��y�����𽥼��ٵġ�
	private float startHeight = TabDemoActivity.HEIGHT-80;
	private float endHeight = startHeight;
	// ��״ͼ�Ŀ��
	private int viewWidth = 60;
	
	// ��״ͼ�ĸ߶� ����ʾ����ֵ��100 Ϊ 100%��
	private int maxSize = 43;
	private int indexSize = 0;
	// Ҫ��ʾ��ģʽ��X��ֵ �����ͣ����磺��Ͱٷֱȵȡ�
	private String displayMode = "%";
	
	
	// ģʽ
	private boolean mode = false;
	
	
	
	// ��ͼ����
	private boolean display = true;
	// �Ƿ�������Ч��
	private boolean animMode = true;

	
	
	
	
	
	
	
	/**
	 * 
	 * @param context
	 * @param maxSize ��Ҫ��ʾ����ֵ
	 * @param displayMode ��ʾ������
	 */
	public ConfigurationView(Context context, int maxSize, String displayMode) {
		super(context);
		this.maxSize = maxSize;
		this.displayMode = displayMode;
		init();
	}
	
	/**
	 * 
	 * @param context
	 * @param maxSize		��Ҫ��ʾ����ֵ
	 * @param displayMode	��ʾ������
	 * @param mode			��ʾ��ģʽ��Ĭ��Ϊfalse����ֵԽ�ߣ���ɫԽƫ���ɫ��Ϊtrue��֮
	 */
	public ConfigurationView(Context context, int maxSize, String displayMode, boolean mode) {
		super(context);
		this.maxSize = maxSize;
		this.displayMode = displayMode;
		this.mode = mode;
		init();
	}
	
	/**
	 * 
	 * @param context
	 * @param maxSize		��Ҫ��ʾ����ֵ
	 * @param displayMode	��ʾ������
	 * @param mode			��ʾ��ģʽ��Ĭ��Ϊfalse����ֵԽ�ߣ���ɫԽƫ���ɫ��Ϊtrue��֮
	 * @param animMode 		�Ƿ���ʾ��������Ч����Ĭ��Ϊtrue
	 */
	public ConfigurationView(Context context, int maxSize, String displayMode, boolean mode, boolean animMode) {
		super(context);
		this.maxSize = maxSize;
		this.displayMode = displayMode;
		this.mode = mode;
		this.animMode = animMode;
		init();
	}

	
	
	
	
	
	
	
	
	
	// ���ƽ���
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawRect(10, endHeight, 10+viewWidth, startHeight, paint);
		
		
		if(!display){
			if(!mode && indexSize >= 50){
				paint.setARGB(255, 200, 200, 60);
				if(!mode && indexSize >= 80){
					paint.setARGB(255, (indexSize<100)?(110+indexSize+45):255, (indexSize<100)?210-(indexSize+45):0, 20);
				}
			}else if(mode && indexSize <= 50){
				paint.setARGB(255, 200, 200, 60);
				if(mode && indexSize <= 30){
					paint.setARGB(255, 255-indexSize, indexSize, 20);
				}
			}
			
			
			canvas.drawRect(10, endHeight, 10+viewWidth, startHeight, paint);
			paint.setARGB(255, 255, 255, 255);
//			��ʾbar chart������ַ�
			canvas.drawText("  "+(-113+2*indexSize)+"dbm", numWidth, endHeight-5, paint);
			paint.setARGB(255, 110, 210, 60);
		}
//		��������123��λ��
		canvas.drawText(displayMode, 25, startHeight+15, font_Paint);
	}
	
	
	
	
	
	
	
	
	
	
	
	// ��ʼ��
	private void init(){
		// ��ֵ��ʼ��
		paint = new Paint();
		paint.setARGB(255, 110, 210, 20);
		
//		����123����ɫ
		font_Paint = new Paint();
		font_Paint.setARGB(255, 255, 255, 255);
		
		
		
		
		// ������ֵ��ʾ��ƫ��������ֵԽС��ƫ����Խ��Ҳ����bar chart������ַ�
		numWidth = 9;
		if(maxSize < 10){
			numWidth = 15;
		}else if(maxSize < 100){
			numWidth = 12;
		}
		
		
		
		
		if(animMode){
			// ����һ���߳���ʵ����״ͼ�������ߣ��ҿ϶���Ҫʹ�ö�����
			thread.start();
		}else{
			display = false;
			indexSize = maxSize;
			endHeight = startHeight-(float) (maxSize*1.5);
			invalidate();
		}
	}

	
	
	
	
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 1 && indexSize < maxSize && endHeight >= 20){
				endHeight -= 1.5;
				indexSize += 1;
			}else{
				display = false;
			}
			invalidate();
		}
	};
	
	
	
	
	
	
	
	private Thread thread = new Thread(){
		@Override
		public void run(){
			while(!Thread.currentThread().isInterrupted() && display )
			{
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
				try {
					Thread.sleep(15);
				} catch (InterruptedException e) {
					System.err.println("InterruptedException���̹߳ر�");
					this.interrupt();
				}
			}
		}
	};
	
	
	
	
	
	// ˢ��View
	public void toInvalidate(){
		this.invalidate();
	}
}
