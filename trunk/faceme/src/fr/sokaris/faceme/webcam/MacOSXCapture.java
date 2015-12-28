/*
 *  This file is part of FaceMe.
 *
 *  FaceMe is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  FaceMe is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with FaceMe; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *  
 *  Author: Sylvain Maucourt, smaucourt@gmail.com
 */

package fr.sokaris.faceme.webcam;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Timer;
import java.util.TimerTask;

import quicktime.QTException;
import quicktime.QTSession;
import quicktime.qd.QDException;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.std.StdQTException;
import quicktime.std.image.CodecComponent;
import quicktime.std.image.DSequence;
import quicktime.std.image.ImageDescription;
import quicktime.std.image.Matrix;
import quicktime.std.image.QTImage;
import quicktime.std.sg.SGChannel;
import quicktime.std.sg.SGDataProc;
import quicktime.std.sg.SGVideoChannel;
import quicktime.std.sg.SequenceGrabber;
import quicktime.util.QTPointerRef;
import quicktime.util.RawEncodedImage;

public class MacOSXCapture implements Capture {

	private SequenceGrabber sg;
	private SGVideoChannel vc;
	private QDRect cameraImageSize;
	private QDGraphics gWorld;
	private int myCodec;
	private int[] pixelData;
	private BufferedImage image;
	private Timer t = new Timer();

	public void start() throws WebCamException {
		try {
			QTSession.open();
			sg = new SequenceGrabber();
			vc = new SGVideoChannel(sg);
			cameraImageSize = new QDRect(320, 240); // vc.getSrcVideoBounds();
			gWorld = new QDGraphics(cameraImageSize);
			initImage();
			initSequenceGrabber();
			initCamera();
		} catch (QTException e) {
			throw new WebCamException(e);
		}
	}

	public void stop() {
		t.cancel();
		QTSession.close();
	}

	private void initCamera() throws WebCamException {
		TimerTask idleCamera = new TimerTask() {

			@Override
			public void run() {
				try {
					sg.idle();
				} catch (StdQTException e) {
				}
			}
		};

		t.schedule(idleCamera, 0, 50);
	}

	private void initSequenceGrabber() throws WebCamException {
		SGDataProc myDataProc;
		try {
			myDataProc = new SGDataProc() {
				DSequence ds = null;
				final Matrix idMatrix = new Matrix();
				byte[] rawData = new byte[QTImage.getMaxCompressionSize(gWorld,
						gWorld.getBounds(), 0,
						quicktime.std.StdQTConstants.codecLowQuality, myCodec,
						CodecComponent.anyCodec)];

				public int execute(SGChannel chan, QTPointerRef dataToWrite,
						int offset, int chRefCon, int time, int writeType) {
					if (chan instanceof SGVideoChannel)
						try {
							ImageDescription id = vc.getImageDescription();
							if (rawData == null)
								rawData = new byte[dataToWrite.getSize()];
							RawEncodedImage ri = new RawEncodedImage(rawData);
							dataToWrite.copyToArray(0, rawData, 0, dataToWrite
									.getSize());
							if (ds == null) {
								ds = new DSequence(
										id,
										ri,
										gWorld,
										cameraImageSize,
										idMatrix,
										null,
										0,
										quicktime.std.StdQTConstants.codecNormalQuality,
										CodecComponent.anyCodec);
							} else {
								ds
										.decompressFrameS(
												ri,
												quicktime.std.StdQTConstants.codecNormalQuality);
							}
							gWorld.getPixMap().getPixelData().copyToArray(0,
									pixelData, 0, pixelData.length);
							return 0;

						} catch (Exception ex) {

							ex.printStackTrace();
							return 1;

						}
					else
						return 1;
				}

			};

			sg.setDataProc(myDataProc);

			// Preparing for output
			sg.setDataOutput(null,
					quicktime.std.StdQTConstants.seqGrabDontMakeMovie);
			sg.prepare(false, true);
			sg.startRecord();
		} catch (StdQTException e) {
			throw new WebCamException(e);
		} catch (QDException e) {
			throw new WebCamException(e);
		} catch (QTException e) {
			throw new WebCamException(e);
		}

	}

	private void initImage() throws WebCamException {
		try {
			sg.setGWorld(gWorld, null);
			vc.setBounds(cameraImageSize);
			vc.setUsage(quicktime.std.StdQTConstants.seqGrabRecord);
			vc.setFrameRate(24);
			myCodec = quicktime.std.StdQTConstants.kComponentVideoCodecType;
			vc.setCompressorType(myCodec);

			// Setting up the buffered image
			int size = gWorld.getPixMap().getPixelData().getSize();
			int intsPerRow = gWorld.getPixMap().getPixelData().getRowBytes() / 4;
			size = intsPerRow * cameraImageSize.getHeight();
			pixelData = new int[size];
			DataBuffer db = new DataBufferInt(pixelData, size);
			ColorModel colorModel = new DirectColorModel(32, 0x00ff0000,
					0x0000ff00, 0x000000ff);
			int[] masks = { 0x00ff0000, 0x0000ff00, 0x000000ff };
			// int[] masks = {0x00000000, 0x00000000, 0x00000000};
			WritableRaster raster = Raster.createPackedRaster(db,
					cameraImageSize.getWidth(), cameraImageSize.getHeight(),
					intsPerRow, masks, null);
			image = new BufferedImage(colorModel, raster, false, null);
		} catch (QTException e) {
			throw new WebCamException(e);
		}
	}

	public int getHeight() {
		return cameraImageSize.getHeight();
	}

	public int getWidth() {
		return cameraImageSize.getWidth();
	}

	public BufferedImage showSource() throws WebCamException {
		if (image == null) {
			throw new WebCamException(new NullPointerException());
		}
		return image;
	}

}
