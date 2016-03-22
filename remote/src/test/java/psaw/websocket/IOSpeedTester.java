package psaw.websocket;

import java.io.*;
import java.awt.image.DataBufferByte;
import java.nio.channels.FileChannel;
import java.nio.*;
import java.util.Random;
import java.nio.ByteBuffer;


class IOSpeedTester {

    public static void main(String[] args) throws IOException {
        new IOSpeedTester();
    }

    Random g;

    int[] j;

    int rep = 1;

    long t1, t2;

    boolean ra = false;


    byte[] b;

    DataBufferByte dbb;

    RandomAccessFile raf;

    FileInputStream fis;

    BufferedInputStream bis;

    ByteArrayInputStream bais;

    MappedByteBuffer mbb;

    FileChannel fc;

    ByteBuffer bb;

    public IOSpeedTester() throws IOException {


        File file = new File("world_2.mp3");  // 3 MEG
        //File file = new File("05Oriento.mp3"); //7 MEG
        //File file = new File("13MartianGarageParty.wav"); //30 MEG

        int ln = (int) (file.length());
        g = new Random();


        int tt = 0;


        ///////////////////////////try {


        for (int rr = 0; rr < 1; rr++) {
            t1 = System.currentTimeMillis();

            fis = new FileInputStream(file);
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            raf = new RandomAccessFile(file, "rw");
            fis = new FileInputStream(file);
            b = new byte[ln];
            fis.read(b);        // fis.read(b,0,ln);
            fis = new FileInputStream(file);
            b = new byte[ln];
            fis.read(b, 0, ln);
            bais = new ByteArrayInputStream(b);
            fis = new FileInputStream(file);
            b = new byte[ln];
            fis.read(b, 0, ln);
            dbb = new DataBufferByte(b, ln);
            fis = new FileInputStream(file);
            fc = fis.getChannel();
            mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, ln);
            fis = new FileInputStream(file);
            bb = ByteBuffer.allocate(32000000);
            b = new byte[ln];
            fis.read(b);
            bb.put(b);

            //360
            t2 = System.currentTimeMillis();

            tt += (t2 - t1);
            System.out.println("initialize: " + (t2 - t1));
            //b=null; //fis=null;
        }
        System.out.println("Final initialize: " + tt / 10);

        // was "<10000"


        if (ra) {
            j = new int[ln];
            for (int qq = 0; qq < ln; qq += 1) {
                //j[n]=1;	//j[n]=500000;		//j[n]=n;		//j[n]=ln-n-1;		//for (int po=0 ; po<1 ;po++ ) {	//}
                j[qq] = g.nextInt(ln);
            }


            //Unquote me for slowdown if qq<10000:
            for (int n = 0; n < 3993; n++) {
            }
        }


        //Thread.sleep(10000);

        ///////////////////////////} catch (Exception e){ System.out.println("Exception :" + e);}


        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////


        /////////// BufferedInputStream

        tt = 0;
        for (int rr = 0; rr < rep; rr++) {
            t1 = System.currentTimeMillis();
            int i;
            for (int n = 0; n < ln; n += 1) {
                i = bis.read();
            }

            t2 = System.currentTimeMillis();
            System.out.print((t2 - t1) + " ");
            tt += (t2 - t1);
        }
        System.out.println(".....BufferedInputStream read: " + tt / rep);
        System.out.println();


        ///////////// ByteArrayInputStream

        tt = 0;
        for (int rr = 0; rr < rep; rr++) {
            t1 = System.currentTimeMillis();
            int i;
            for (int n = 0; n < ln; n += 1) {
                i = bais.read();
            }

            t2 = System.currentTimeMillis();
            System.out.print((t2 - t1) + " ");
            tt += (t2 - t1);
        }
        System.out.println(".....ByteArrayInputStream read: " + tt / rep);
        System.out.println();


        //////////// DataBufferByte

        tt = 0;
        for (int rr = 0; rr < rep; rr++) {
            t1 = System.currentTimeMillis();
            int i;
            for (int n = 0; n < ln; n += 1) {
                i = dbb.getElem(n);
            }

            t2 = System.currentTimeMillis();
            System.out.print((t2 - t1) + " ");
            tt += (t2 - t1);
        }
        System.out.println(".....DataBufferByte read: " + tt / rep);

        tt = 0;
        for (int rr = 0; rr < rep; rr++) {
            t1 = System.currentTimeMillis();
            int i;
            for (int n = 0; n < ln; n += 1) {
                dbb.setElem(n, 50);
            }

            t2 = System.currentTimeMillis();
            System.out.print((t2 - t1) + " ");
            tt += (t2 - t1);
        }
        System.out.println(".....DataBufferByte write: " + tt / rep);

        if (ra == true) {
            tt = 0;
            for (int rr = 0; rr < rep; rr++) {
                t1 = System.currentTimeMillis();
                int i;
                for (int n = 0; n < ln; n += 1) {
                    i = dbb.getElem(j[n]);
                }

                t2 = System.currentTimeMillis();
                System.out.print((t2 - t1) + " ");
                tt += (t2 - t1);
            }
            System.out.println(".....DataBufferByte RAread: " + tt / rep);
        }


        if (ra == true) {
            tt = 0;
            for (int rr = 0; rr < rep; rr++) {
                t1 = System.currentTimeMillis();
                int i;
                for (int n = 0; n < ln; n += 1) {
                    dbb.setElem(j[n], 50);
                }

                t2 = System.currentTimeMillis();
                System.out.print((t2 - t1) + " ");
                tt += (t2 - t1);
            }
            System.out.println(".....DataBufferByte RAwrite: " + tt / rep);
        }
        System.out.println();


        ///////////////////////// ByteBuffer

        tt = 0;
        for (int rr = 0; rr < rep; rr++) {
            t1 = System.currentTimeMillis();
            int i;
            for (int n = 0; n < ln; n += 1) {
                i = bb.get(n);
            }

            t2 = System.currentTimeMillis();
            System.out.print((t2 - t1) + " ");
            tt += (t2 - t1);
        }
        System.out.println(".....ByteBuffer read: " + tt / rep);


        tt = 0;
        for (int rr = 0; rr < rep; rr++) {
            t1 = System.currentTimeMillis();
            int i;
            for (int n = 0; n < ln; n += 1) {
                bb.put(n, (byte) 50);
            }

            t2 = System.currentTimeMillis();
            System.out.print((t2 - t1) + " ");
            tt += (t2 - t1);
        }
        System.out.println(".....ByteBuffer write: " + tt / rep);

        if (ra == true) {
            tt = 0;
            for (int rr = 0; rr < rep; rr++) {
                t1 = System.currentTimeMillis();
                int i;
                for (int n = 0; n < ln; n += 1) {
                    i = bb.get(j[n]);
                }

                t2 = System.currentTimeMillis();
                System.out.print((t2 - t1) + " ");
                tt += (t2 - t1);
            }
            System.out.println(".....ByteBuffer RA read: " + tt / rep);
        }

        if (ra == true) {
            tt = 0;
            for (int rr = 0; rr < rep; rr++) {
                t1 = System.currentTimeMillis();
                int i;
                for (int n = 0; n < ln; n += 1) {
                    bb.put(j[n], (byte) 50);
                }

                t2 = System.currentTimeMillis();
                System.out.print((t2 - t1) + " ");
                tt += (t2 - t1);
            }
            System.out.println(".....ByteBuffer write: " + tt / rep);
        }
        System.out.println();


        ///////////////////// RandomAccessFile

        tt = 0;
        for (int rr = 0; rr < rep; rr++) {
            t1 = System.currentTimeMillis();
            int i;
            for (int n = 0; n < ln; n += 1) {
                raf.read();
            }

            t2 = System.currentTimeMillis();
            System.out.print((t2 - t1) + " ");
            tt += (t2 - t1);
        }
        System.out.println(".....RandomAccessFile read: " + tt / rep);


        //HD file writing - be careful!
        //tt=0;
        //for (int rr=0 ; rr<rep ; rr++ ) {
        //	t1 = System.currentTimeMillis();
        //	int i;
        //	for (int n=0 ; n<ln ;n+=1 ) {
        //		raf.write(50);
        //	}
        //
        //	t2 = System.currentTimeMillis();
        //	System.out.print((t2-t1)+" ");
        //	tt+=(t2-t1);
        //}
        //System.out.println(".....RandomAccessFile write: "+tt/rep);


        if (ra == true) {
            tt = 0;
            for (int rr = 0; rr < rep; rr++) {
                t1 = System.currentTimeMillis();
                int i;
                for (int n = 0; n < ln; n += 1) {
                    raf.seek(j[n]);
                    raf.read();
                }

                t2 = System.currentTimeMillis();
                System.out.print((t2 - t1) + " ");
                tt += (t2 - t1);
            }
            System.out.println(".....RandomAccessFile RAread: " + tt / rep);
        }


        //if (ra==true) {
        //HD file writing - be careful!
        //tt=0;
        //for (int rr=0 ; rr<rep ; rr++ ) {
        //	t1 = System.currentTimeMillis();
        //	int i;
        //	for (int n=0 ; n<ln ;n+=1 ) {
        //		raf.seek(j[n]);  raf.write(50);
        //	}
        //
        //	t2 = System.currentTimeMillis();
        //	System.out.print((t2-t1)+" ");
        //	tt+=(t2-t1);
        //}
        //System.out.println(".....RandomAccessFile RAwrite: "+tt/rep);
        //}
        System.out.println();


        //////////// byte[]


        tt = 0;
        for (int rr = 0; rr < rep; rr++) {
            t1 = System.currentTimeMillis();
            int i;
            for (int n = 0; n < ln; n += 1) {
                i = b[n];
            }

            t2 = System.currentTimeMillis();
            System.out.print((t2 - t1) + " ");
            tt += (t2 - t1);
        }
        System.out.println(".....byte[] read: " + tt / rep);


        if (ra == true) {
            tt = 0;
            for (int rr = 0; rr < rep; rr++) {
                t1 = System.currentTimeMillis();
                int i;
                for (int n = 0; n < ln; n += 1) {
                    i = b[j[n]];
                }

                t2 = System.currentTimeMillis();
                System.out.print((t2 - t1) + " ");
                tt += (t2 - t1);
            }
            System.out.println(".....byte[] RAread: " + tt / rep);
        }


        tt = 0;
        for (int rr = 0; rr < rep; rr++) {
            t1 = System.currentTimeMillis();
            int i;
            for (int n = 0; n < ln; n += 1) {
                b[n] = (byte) 50;
            }

            t2 = System.currentTimeMillis();
            System.out.print((t2 - t1) + " ");
            tt += (t2 - t1);
        }
        System.out.println(".....byte[] write: " + tt / rep);

        if (ra == true) {
            tt = 0;
            for (int rr = 0; rr < rep; rr++) {
                t1 = System.currentTimeMillis();
                int i;
                for (int n = 0; n < ln; n += 1) {
                    b[j[n]] = (byte) 50;
                }

                t2 = System.currentTimeMillis();
                System.out.print((t2 - t1) + " ");
                tt += (t2 - t1);
            }
            System.out.println(".....byte[] RAwrite: " + tt / rep);
        }
        System.out.println();


        ///////////////// MappedByteBuffer

        tt = 0;
        for (int rr = 0; rr < rep; rr++) {
            t1 = System.currentTimeMillis();
            int i;
            for (int n = 0; n < ln; n += 1) {
                i = mbb.get(n);
            }

            t2 = System.currentTimeMillis();
            System.out.print((t2 - t1) + " ");
            tt += (t2 - t1);
        }
        System.out.println(".....MappedByteBuffer read: " + tt / rep);


        if (ra == true) {
            tt = 0;
            for (int rr = 0; rr < rep; rr++) {
                t1 = System.currentTimeMillis();
                int i;
                for (int n = 0; n < ln; n += 1) {
                    i = mbb.get(j[n]);
                }

                t2 = System.currentTimeMillis();
                System.out.print((t2 - t1) + " ");
                tt += (t2 - t1);
            }
            System.out.println(".....MappedByteBuffer RA read: " + tt / rep);
        }
        System.out.println();


        //////////// FileInputStream

        tt = 0;
        for (int rr = 0; rr < rep; rr++) {
            t1 = System.currentTimeMillis();
            int i;
            for (int n = 0; n < ln; n += 1) {
                i = fis.read();
            }

            t2 = System.currentTimeMillis();
            System.out.print((t2 - t1) + " ");
            tt += (t2 - t1);
        }
        System.out.println(".....FileInputStream read: " + tt / rep);
        System.out.println();


    }
}


//Scrap:
//				//b=new byte[ln];         for (int n=0 ; n<ln ;n++ ) {	b[n]=(byte)(bis.read()); }  //or.....
