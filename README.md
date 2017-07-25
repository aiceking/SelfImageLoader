# LIFOandFIFOImageLoader

线程池异步加载图片（对图片进行压缩，内存控制）

可LIFO加载（后进先出）快速滑动时，优先加载当前页面显示的图片，防止等待

可FIFO加载（先进先出）

四级缓存（内存缓存，磁盘缓存，文件缓存，网络缓存）

![Image text](https://github.com/AndroidCloud/LIFOandFIFOImageLoader/blob/master/DemoImg/GIF.gif)
