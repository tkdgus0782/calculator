import java.awt.*;
import java.awt.event.*;
import java.util.Objects;
@SuppressWarnings("serial")


public class Calculator_20191613 extends Frame implements ActionListener{	
	private String[] input;//입력
	private int length;
	private String[] res;//결과
	
	private boolean isMin;
	private boolean isNum;
	private boolean isFin;
	private String[] stack1;
	private int lengthS1;
	private String[] stack2;
	private int lengthS2;
	
	private double[] stack3;
	private int lengthS3;
	
	private boolean err;
	
	private MyCanvas c1;
	private MyCanvas c2;
	
	
	Calculator_20191613(String name){
		super(name);
		input = new String[200];
		res = new String[2];
		stack1 = new String[200];
		stack2 = new String[200];
		stack3 = new double[200];
    	init();

		setLayout(new BorderLayout());
      	//////////////////
      	c1 = new MyCanvas(input, new Color(80, 188, 223));
      	c2 = new MyCanvas(res, new Color(180, 188, 223));
      	//c.setSize(1000,100);
      	////////////////////////////
      	Panel b = new Panel();//button
      	b.setSize(1000,400);
        b.setLayout(new GridLayout(5,5));

        Button fact = new Button("!"); b.add(fact); fact.addActionListener(this);//앞에 숫자 필요      
        Button parb = new Button("("); b.add(parb); parb.addActionListener(this);//
        Button pare = new Button(")"); b.add(pare); pare.addActionListener(this);//
        Button per = new Button("%"); b.add(per);   per.addActionListener(this);//퍼센트인데, 그냥 *0.01이랑 동일
        Button del = new Button("AC"); b.add(del);   del.addActionListener(this);//지우기
          
        Button loge = new Button("ln"); b.add(loge); loge.addActionListener(this);//ln인데, 뒤에 숫자 필요. 괄호 닫아줘야 앞에 숫자있으면 곱하기가 있는 것으로 취급
        Button num7 = new Button("7"); b.add(num7);  num7.addActionListener(this);
        Button num8 = new Button("8"); b.add(num8);  num8.addActionListener(this);   
        Button num9 = new Button("9"); b.add(num9);  num9.addActionListener(this);
        Button div = new Button("/"); b.add(div);    div.addActionListener(this);//앞 뒤로 숫자 필요
         
        Button log10 = new Button("log"); b.add(log10); log10.addActionListener(this);//log인데, 뒤에 숫자필요. 괄호 닫아줘야  앞에 숫자있으면 곱하기가 있는 것으로 취급
        Button num4 = new Button("4"); b.add(num4);    num4.addActionListener(this);
        Button num5 = new Button("5"); b.add(num5);    num5.addActionListener(this);
        Button num6 = new Button("6"); b.add(num6);    num6.addActionListener(this);
        Button mul = new Button("x"); b.add(mul);      mul.addActionListener(this);//앞 뒤로 숫자 필요
          
        Button root = new Button("root"); b.add(root); root.addActionListener(this);//루트인데, 뒤에 숫자필요. 괄호 닫아줘야.앞에 숫자있으면 곱하기가 있는 것으로 취급
        Button num1 = new Button("1"); b.add(num1);  num1.addActionListener(this);
        Button num2 = new Button("2"); b.add(num2);  num2.addActionListener(this);
        Button num3 = new Button("3"); b.add(num3);  num3.addActionListener(this);
        Button sub = new Button("-"); b.add(sub);    sub.addActionListener(this);//-인데, 빼기랑 마이너스 두가지 기능. 부호의 경우 -1*랑 동일
         
        Button pow = new Button("^"); b.add(pow);   pow.addActionListener(this);//제곱인데, 뒤에 숫자 필요. 괄호 닫아줘야.
        Button num0 = new Button("0"); b.add(num0); num0.addActionListener(this);
        Button dot = new Button("."); b.add(dot);   dot.addActionListener(this);//소수점. 앞 뒤중 하나 이상의 숫자 필요. 각 자리 없으면 0.~ 혹은 ~.0 으로 취급
        Button equ = new Button("="); b.add(equ);   equ.addActionListener(this); equ.setBackground(new Color(80, 188, 222));//연산 시작.
        Button add = new Button("+"); b.add(add);   add.addActionListener(this);
        /////////////////////////////////////////////////////////
        setBackground(new Color(255,255,255));
        add(c1, BorderLayout.NORTH);//계산할 식
        add(c2, BorderLayout.CENTER);//계산할 식
        add(b, BorderLayout.SOUTH);//버튼
        pack();
	}
	//계산기 초기화
	
	public static void main(String[] args) {
		Frame f = new Calculator_20191613("Calculator_20191613");  
		f.setSize(1000, 500);
      	WindowDestroyer listener = new WindowDestroyer();  
      	f.addWindowListener(listener);
      	f.setVisible(true); 
	}

	public void actionPerformed(ActionEvent e){
	    String cmd = e.getActionCommand();	 
	    
	    if(Objects.equals("AC", cmd)) {//ALL CLEAR
	    	init();
	    	c1.update(0);
	    	c2.update(0);
	    }
	    else if(isFin) {
	    	return;
	    }
	    else if(Objects.equals("=", cmd)) {//결과값 또는 에러 출력
	    	parse();
	    	double result = calc();
	    	if(err) {
	    		System.out.println("에러");
	    		res[0] = new String("error");
	    	}
	    	else if(result == Math.rint(result)) {
	    		System.out.println("답은: " + (int)result);
	    		res[0] = new String(( Integer.toString((int)result)));
	    	}
	    	else {
	    		System.out.println("답은: " + result);
	    		res[0] = new String(( Double.toString(result)));
	    	}	
	    	isFin = true;
	    	c2.update(1);
	    }
	    else if(length == 0) {//-,괄호, log, ln, root, 숫자면 맨 앞 알때 0 안붙음
	    	if(getPriority(cmd) == 0 || getPriority(cmd) == 3 || getPriority(cmd) == -2 || Objects.equals("-", cmd)) {
	    		input[length++] = cmd;
	    	}
	    	else if(getPriority(cmd) == -1) {//.이면 0.으로 해줘야함.
	    		input[length++] = "0.";
	    	}
	    	else {
	    		input[length++] = "0";
	    		input[length++] = cmd;
	    	}
	    	if(Objects.equals(cmd,"-")){
	    		isMin = true;
	    	}
	    }
	    else {
	    	if(isNum) {//전에 숫자거나 점이고
	    		if(getPriority(cmd) <= -1) {//지금도 숫자거나 점
	    			input[length-1] += cmd;
	    		}
	    		else if(Objects.equals(cmd,"(") || (getPriority(cmd) == 3)) {//여는 괄호나 log, ln, root경우에는 숫자가 앞에 있으면 곱하기 붙여줘야함.
	    			input[length++] = "x";
	    			input[length++] = cmd;
	    		}
	    		else {
	    			input[length++] = cmd;
	    		}
	    	}
	    	else if(isMin) {
	    		if(getPriority(cmd) <= -1) {//지금도 숫자거나 점
	    			input[length-1] += cmd;
	    		}
	    		else if(Objects.equals(cmd,"(") || (getPriority(cmd) == 3)) {//여는 괄호나 log, ln, root경우에는 숫자가 앞에 있으면 곱하기 붙여줘야함.
	    			input[length-1] += "1";
	    			input[length++] = "x";
	    			input[length++] = cmd;
	    		}
	    		else {
	    			input[length++] = cmd;
	    		}
	    		isMin = false;
	    	}
	    	else{
	    		input[length++] = cmd;
	    	}
	    }
	    
	    
	    
	    
	    if(getPriority(cmd) == 3) {//log, ln, root 면 자동으로 괄호 열어주기
	    	input[length++] = "(";
	    }
	    
	    
	    if(!isNum && Objects.equals("-", cmd)) {
	    	isMin = true;
	    }
	    
	    if(Objects.equals(")", cmd) || getPriority(cmd) <= -1) {//숫자거나 점이면 on
	    	isNum = true;
	    }
	    else {
	    	isNum =false;
	    }
	    
	   
	    
	    c1.update(length);
	    System.out.print("now: ");
		for(int i=0;i<length;i++) {
			System.out.print("["+input[i]+"]");
		}
		System.out.println();
		
		
		
		
	}
	
	public int getPriority(String a) {
		if(Objects.equals(a, "(") || Objects.equals(a,")")) {
			return 0;
		}
		else if(Objects.equals(a, "!") || Objects.equals(a, "%")){
			return 5;
		}
		else if(Objects.equals(a, "^")) {
			return 4;
		}
		else if(Objects.equals(a, "log") || Objects.equals(a,"ln") || Objects.equals(a, "root")) {//앞에 아무 것도 없이 처음에 나와도 ok
			return 3;
		}
		else if(Objects.equals(a, "x") || Objects.equals(a, "/")) {
			return 2;
		}
		else if(Objects.equals(a, "+") || Objects.equals(a, "-")) {
			return 1;
		}
		else if(Objects.equals(a, ".")) {
			return -1;
		}
		return -2;//숫자
	}
	
	public void parse() {
		for(int i=0; i<length; i++) {
			int nowP = getPriority(input[i]);
			
			if(nowP == -2) {//숫자
				stack2[lengthS2++] = input[i];
			}
			else if(Objects.equals("(", input[i])){
				stack1[lengthS1++] = input[i];
			}
			else if(Objects.equals(")", input[i])) {
				while(lengthS1 > 0 && !Objects.equals("(", stack1[lengthS1-1])) {
					stack2[lengthS2++] = stack1[lengthS1-1];
					lengthS1--;
				}
				if(lengthS1 >0) {
					lengthS1--;
				}
			}
			else {//그외 기호
				while(lengthS1 > 0 && nowP <= getPriority(stack1[lengthS1-1])) {
					stack2[lengthS2++] = stack1[lengthS1-1];
					lengthS1--;
				}
				stack1[lengthS1++] = input[i];
			}
		}
		
		while(lengthS1 > 0) {
			stack2[lengthS2++] = stack1[lengthS1-1];
			lengthS1--;
		}
		
		for(int i=0;i<lengthS2;i++) {
			System.out.print("["+stack2[i]+"]");
		}
		System.out.println();
	 }

	public double calc() {
		for(int i=0; i<lengthS2; i++) {
			String a = stack2[i];
			
			if(getPriority(a) == -2) {//숫자
				double temp = Double.parseDouble(a);
				stack3[lengthS3++] = temp;
			}
			else if(Objects.equals(a, "!")) {
				if(checkS3(1)) {
					double result = 1;
					int temp=(int)stack3[lengthS3-1];
					for(int j=1;j<=temp;j++) {
						result *= j;
					}
					stack3[lengthS3-1] = result;
				}
			}
			else if(Objects.equals(a, "%")){
				if(checkS3(1)) {
					stack3[lengthS3-1] = stack3[lengthS3-1]*0.01;
				}			
			}
			else if(Objects.equals(a, "^")) {
				if(checkS3(2)) {
					stack3[lengthS3-2] = Math.pow(stack3[lengthS3-2], stack3[lengthS3-1]);
					lengthS3--;
				}
			}
			else if(Objects.equals(a, "log")) {
				if(checkS3(1)) {
					if(stack3[lengthS3-1] >= 0) {
						stack3[lengthS3-1] = Math.log10(stack3[lengthS3-1]);
					}
					else {
						err = true;
					}
				}
			}
			else if(Objects.equals(a, "ln")) {
				if(checkS3(1)) {
					if(stack3[lengthS3-1] >= 0) {
						stack3[lengthS3-1] = Math.log(stack3[lengthS3-1]);
					}
					else {
						err = true;
					}
					
				}
			}
			else if(Objects.equals(a, "root")) {
				if(checkS3(1)) {
					if(stack3[lengthS3-1] >= 0) {
						stack3[lengthS3-1] = Math.sqrt(stack3[lengthS3-1]);
					}	
					else {
						err = true;
					}
				}
			}
			else if(Objects.equals(a, "x")) {
				if(checkS3(2)) {
					stack3[lengthS3-2] *= stack3[lengthS3-1];
					lengthS3--;
				}
			}
			else if(Objects.equals(a, "/")) {
				if(checkS3(2)) {
					if(stack3[lengthS3-1] != 0) {
						stack3[lengthS3-2] /= stack3[lengthS3-1];
						lengthS3--;
					}
					else {
						err = true;
					}
				}
			}
			else if(Objects.equals(a, "+")) {
				if(checkS3(2)) {
					stack3[lengthS3-2] += stack3[lengthS3-1];
					lengthS3--;
				}
				
			}
			else if(Objects.equals(a, "-")) {
				if(checkS3(2)) {
					stack3[lengthS3-2] -= stack3[lengthS3-1];
					lengthS3--;
				}
			}
		}
		
		if(lengthS3 != 1) {
			err = true;
		}
		
		return stack3[0];
	}
		
	public void init() {
    	input[0] = "0";
    	length = 0;
    	lengthS1 = 0;
    	lengthS2 = 0;
    	lengthS3 = 0;
    	isNum = false;
    	err = false;
    	isFin = false;
    	isMin = false;
	}
	
	public boolean checkS3(int k) {
		if(lengthS3 < k ) {
			err = true;
			return false;
		}
		return true;
	}
}

class MyCanvas extends Canvas{
	private String[] contents;
	private int length;
	MyCanvas(String[] s, Color a){
		super();
		this.setBackground(a);
		this.setSize(200,180);
		this.contents = s;
		this.length = 0;
	}
	public void paint(Graphics g) {
		Font f = new Font("바탕", Font.PLAIN, 30);
		g.setFont(f);
		g.clearRect(0,0,1000,1000);
		String temp = new String();
		for(int i=0;i<length;i++) {
			temp += contents[i];
		}
		g.drawString(temp, 20, 50);
		if(length == 0) {
			g.drawString("0", 20, 50);
		}
	}
	public void update(int len) {
		this.length = len;
		repaint();
	}
}

class WindowDestroyer extends WindowAdapter
{
    public void windowClosing(WindowEvent e) 
    {
        System.exit(0);
    }
}
