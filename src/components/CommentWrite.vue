<template>
  <div class="comment_write">
    <slot></slot> <!-- textarea 위치 -->
    <div class="write_btn" @click="uploadComment">
      {{ writeButtonText }}
    </div>
  </div>
</template>

<script>
export default {
  props: {
    writeComment: String,
    mainCommentId: Number
  },
  data() {
    const writeButtonText = this.mainCommentId === undefined ? "댓글쓰기" : "답글쓰기";
    return {
      writeButtonText,
    }
  },
  methods: {
    async uploadComment() {
      // 내용이 공백인 경우
      if (this.writeComment === "") {
        alert("내용을 입력하세요");
        return;
      }

      await this.$store.dispatch('UPLOAD_COMMENT_DATA', {
        text: this.writeComment,
        postId: this.$route.params.postId,
        mainCommentId: this.mainCommentId
      });
      await this.$store.dispatch('GET_COMMENT_DATA', {
        postId: this.$route.params.postId,
      });

      if (this.mainCommentId !== undefined) {
        this.$emit('uploadSubComment')
      }
      this.$emit('initWriteComment');

      this.$store.state.page = 0;
      this.$store.state.infiniteId += 1; // 무한 스크롤 다시 작동
    }
  }
};
</script>

<style lang="scss" scoped>
$button-color: #1a72e6;
$button-hover-color: #4383ee;

.comment_write {
  padding: 0.714em;
  background-color: #f9fafc;

  textarea {
    width: 100%;
    height: 100px;
    border: 1px solid #9ea0ab;
    padding: 0.571em;
    border-radius: 0.286em;
  }

  .write_btn {
    cursor: pointer;
    margin-top: 0.313em;
    width: 100%;
    font-size: 1.143em;
    text-align: center;
    padding: 0.625em 1em;
    background-color: $button-color;
    color: white;
    border-radius: 0.375em;

    &:active {
      background-color: $button-hover-color;
    }
  }
}

</style>