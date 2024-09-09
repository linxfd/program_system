<template>
  <video controls width="640">
    <source :src="videoSrc" type="video/mp4">
    您的浏览器不支持 HTML5 video 标签。
  </video>
</template>

<script>
export default {
  data() {
    return {
      videoSrc: 'http://localhost:9000/program-bucket/video/20240909/3a6fbf9e99e94b1685f7b6e2390a8f42laundry day.mp4'
    }
  }
} 
</script>