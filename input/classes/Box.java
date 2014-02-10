
public class Box {
       private int height;
private static int count = 0;
       public Box(int height) { this.height = height; count++;}
       public int getHeight() {return height;}
       private void setHeight(int height) {this.height = height;}   
       public static int getCount() { return count; }
       public static class Item{
         public int h;
         public int getPid() { return h; }
         public int getQuantity() { return h; }
         public static class Gadget{
        	 void setGadget(String x){}
         }

       }
}