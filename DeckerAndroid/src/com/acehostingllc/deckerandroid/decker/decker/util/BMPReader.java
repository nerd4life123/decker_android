package com.acehostingllc.deckerandroid.decker.decker.util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;
import android.view.View;


/** BMP parser for 24, 8 and 4 bit windows .bmp images
* 	this is a modified version of the method from
* 	   http://www.javaworld.com/javaworld/javatips/jw-javatip43.html
*
* 	reads a image in bmp format from an InputStream into a java.awt.Image object
* 	sets the pixels with argb value "transparent_color" to argb value 0
*
*  info about 16 bit bmps fetched from
*     http://atlc.sourceforge.net/bmp.html
*/

public class BMPReader
{

    /** this method interprets a section of a byte buffer as an integer, low byte first */
    private static int parseValue(byte[] buffer, int position, int bytes)
    {
		int ret = 0;
		for(int i = 0; i < bytes; i++)
			ret |= (((int)buffer[position+i]&0xff)<<(8*i));
		return ret;
	}


	/** this method keeps reading until it has read the requested number of bytes from a stream */
	private static void read(InputStream stream, byte[] buffer, int offset, int length)
	throws IOException
	{
		int bytes_read = 0, new_bytes;
		do{
			new_bytes = stream.read(buffer, offset+bytes_read, length-bytes_read);
			if( new_bytes > -1 )
				bytes_read += new_bytes;
			else
				throw new IOException("could only read "+bytes_read+" of expected "+length+" bytes of pixel data");

			if( bytes_read < length )
				Thread.yield();
		}while(bytes_read < length);
	}

	public static Bitmap readBMP(final InputStream stream, final View component, final int transparent_color)
	{
		//implement alpha
		Options options = new Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap bitmap = convertToMutable(BitmapFactory.decodeStream(stream, null, options));
		for (int x = 0; x < bitmap.getWidth(); x++)
		{
			for (int y = 0; y < bitmap.getHeight(); y++)
			{
				if (bitmap.getPixel(x,  y) == transparent_color)
				{
					bitmap.setPixel(x,  y,  Color.TRANSPARENT);
				}
			}
		}
		return bitmap;
	}
	/**
	 * Converts a immutable bitmap to a mutable bitmap. This operation doesn't allocates
	 * more memory that there is already allocated.
	 * 
	 * @param imgIn - Source image. It will be released, and should not be used more
	 * @return a copy of imgIn, but muttable.
	 */
	public static Bitmap convertToMutable(Bitmap imgIn) {
	    try {
	        //this is the file going to use temporally to save the bytes. 
	        // This file will not be a image, it will store the raw image data.
	        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "temp.tmp");

	        //Open an RandomAccessFile
	        //Make sure you have added uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
	        //into AndroidManifest.xml file
	        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

	        // get the width and height of the source bitmap.
	        int width = imgIn.getWidth();
	        int height = imgIn.getHeight();
	        Config type = imgIn.getConfig();

	        //Copy the byte to the file
	        //Assume source bitmap loaded using options.inPreferredConfig = Config.ARGB_8888;
	        FileChannel channel = randomAccessFile.getChannel();
	        MappedByteBuffer map = channel.map(MapMode.READ_WRITE, 0, imgIn.getRowBytes()*height);
	        imgIn.copyPixelsToBuffer(map);
	        //recycle the source bitmap, this will be no longer used.
	        imgIn.recycle();
	        System.gc();// try to force the bytes from the imgIn to be released

	        //Create a new bitmap to load the bitmap again. Probably the memory will be available. 
	        imgIn = Bitmap.createBitmap(width, height, type);
	        map.position(0);
	        //load it back from temporary 
	        imgIn.copyPixelsFromBuffer(map);
	        //close the temporary file and channel , then delete that also
	        channel.close();
	        randomAccessFile.close();

	        // delete the temp file
	        file.delete();

	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } 

	    return imgIn;
	}
}