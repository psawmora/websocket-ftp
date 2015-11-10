package psaw.websocket;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author prabath.
 */
public class TestFileRead {

    private static String DEFAULT_INPUT_FILE_PATH_1 = "/hms/logs/websocket-ftp/test-file.txt";

    private static String DEFAULT_INPUT_FILE_PATH_2 = "/hms/logs/websocket-ftp/test-file-2.txt";

    private static String DEFAULT_OUTPUT_FILE_PATH_1 = "/hms/logs/websocket-ftp/test-file-out-1.txt";

    private static String DEFAULT_OUTPUT_FILE_PATH_2 = "/hms/logs/websocket-ftp/test-file-out-2.txt";

    private static String DEFAULT_INPUT_FILE_PATH_3 =
            "/home/prabath/Projects/websocket-ftp/core/src/main/resources/call_busy.mp3";

    private static String DEFAULT_INPUT_FILE_PATH_4 =
            "/home/prabath/Projects/websocket-ftp/core/src/main/resources/Priscilla_Ahn_Wallflower.mp3";

    private int bufferSize = 2 * 1024 * 1024;

    public static void main(String[] args) {
        TestFileRead testFileRead = new TestFileRead();
        testFileRead.writeBytesToChannel();
        /*boolean isStopReading = false;
        int filePart = 0;
        while (!isStopReading) {
            FilePartDetail filePartDetail = testFileRead.testRandomAccessFileRead(filePart);
            if (filePartDetail == null || filePartDetail.length <= 0) {
                isStopReading = true;
            }
            filePart++;
            //            System.out.println(filePartDetail);
        }

        System.out.println(filePart);*/

        /*long time = 0;
        int i;
        for (i = 0; i < 1; i++) {
            //            time += testFileRead.writeBytesDirectly();
            time += testFileRead.writeBytesIndirectly();
        }
        System.out.println("Average running time : " + (time / (i + 1)));*/
    }

    private FilePartDetail testRandomAccessFileRead(int filePart) {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(DEFAULT_INPUT_FILE_PATH_1, "r")) {
            byte[] buffer = new byte[bufferSize];
            long length = randomAccessFile.length();
            //            System.out.println(randomAccessFile.length());
            long offset = filePart == 0 ? 0 : (long) filePart * bufferSize;
            randomAccessFile.seek(offset);
            int nReadBytes = randomAccessFile.read(buffer);
            return new FilePartDetail(buffer, nReadBytes);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private long writeBytesToChannel() {
        long startTime = System.currentTimeMillis();
        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(DEFAULT_OUTPUT_FILE_PATH_1), true)) {

            FileChannel outChannel = fileOutputStream.getChannel();
            boolean isStopReading = false;
            int filePart = 0;
            while (!isStopReading) {
                FilePartDetail filePartDetail = testRandomAccessFileRead(filePart);
                if (filePartDetail == null || filePartDetail.length <= 0) {
                    isStopReading = true;
                } else {
                    outChannel.write(ByteBuffer.wrap(filePartDetail.buffer));
                }
                filePart++;
                //            System.out.println(filePartDetail);
            }

            System.out.println(filePart);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Time taken Direct: " + (endTime - startTime));
        return (endTime - startTime);
    }

    private long writeBytesDirectly() {
        long startTime = System.currentTimeMillis();
        try (FileInputStream fileInputStream = new FileInputStream(new File(DEFAULT_INPUT_FILE_PATH_1));
             FileOutputStream fileOutputStream = new FileOutputStream(new File(DEFAULT_OUTPUT_FILE_PATH_1), true)) {

            FileChannel channel = fileInputStream.getChannel();
            FileChannel outChannel = fileOutputStream.getChannel();
            long size = channel.size();
            System.out.println("File Size : " + size);
            long count = 0;
            while (count < size) {
                long position = count == 0 ? 0 : count - 1;
                long transferredAmount = channel.transferTo(position, size - count, outChannel);
                System.out.println("Transferred amount : " + transferredAmount);
                count += transferredAmount;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Time taken Direct: " + (endTime - startTime));
        return (endTime - startTime);
    }

    private long writeBytesIndirectly() {
        long startTime = System.currentTimeMillis();
        File inputFile = new File(DEFAULT_INPUT_FILE_PATH_1);
        File outputFile = new File(DEFAULT_OUTPUT_FILE_PATH_1);
        try (FileChannel inputChannel = new RandomAccessFile(inputFile, "rw").getChannel();
             FileChannel outputChannel = new RandomAccessFile(outputFile, "rw").getChannel()) {

            long size = inputChannel.size();
            MappedByteBuffer outFileMap = outputChannel.map(FileChannel.MapMode.READ_WRITE, 0, Integer.MAX_VALUE);

            ByteBuffer fileBuffer = ByteBuffer.allocate(50000);
            int length;
            while ((length = inputChannel.read(fileBuffer)) > 0) {
                fileBuffer.flip();
                outFileMap.put(fileBuffer);
                fileBuffer.rewind();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Time taken Indirect: " + (endTime - startTime));
        return (endTime - startTime);
    }

    private void writeToFile(long fileSize, String outFileName) {
        int contentArraySize = 1073741824;
        byte[] fileContent = new byte[contentArraySize];
        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(outFileName), true)) {

            FileChannel outChannel = fileOutputStream.getChannel();
            long count;
            System.out.println(fileSize);
            for (count = 0; count < fileSize; count += contentArraySize) {
                System.out.println("Number of written bytes : " + count);
                outChannel.write(ByteBuffer.wrap(fileContent));
            }
            System.out.println(" Value of count : " + count);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void readFile() {
        File file = new File(DEFAULT_INPUT_FILE_PATH_1);
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
            String line;
            while ((line = randomAccessFile.readLine()) != null) {
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class FilePartDetail {

        byte[] buffer;

        int length;

        public FilePartDetail(byte[] buffer, int length) {
            this.buffer = buffer;
            this.length = length;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("FilePartDetail{");
            sb.append("length=").append(length);
            sb.append('}');
            return sb.toString();
        }
    }
}
