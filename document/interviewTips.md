# 问题点

## 性能优化

如果问到性能优化，就先往 大图触发的OOM 方面讲， 
由于有大量客户定制的图片，对于关键的图片，设置图片的时候，通过hook去获取图片的bitmap 对应的尺寸，如果尺寸出入比较大的话，那么就上报一下。