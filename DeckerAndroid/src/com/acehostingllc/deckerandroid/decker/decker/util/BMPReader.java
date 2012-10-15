package com.acehostingllc.deckerandroid.decker.decker.util;
import java.io.InputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
		Bitmap bitmap =  BitmapFactory.decodeStream(stream);
		return bitmap;
	}
}