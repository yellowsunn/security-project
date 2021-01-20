<template>
  <div v-click-outside="initToggle">
    <div class="comment" @click="changeToggle">
      <div class="text">{{ comment.text }}</div>
      <div class="info">
        <span class="writer"><i class="far fa-user"></i> {{ comment.writer }} </span>
        <span class="text_bar"> | </span>
        <span class="time">{{ comment.time }}</span>
      </div>
    </div>
    <div class="sub_write" v-if="toggle">
      <CommentWrite v-on:uploadSubComment="initToggle" :writeComment="subCommentWrite" :mainCommentId="comment.commentId">
        <textarea :value="subCommentWrite" @input="subCommentWrite = $event.target.value" placeholder="답글을 입력해주세요."></textarea>
      </CommentWrite>
    </div>
    <div class="sud_comment" v-for="subComment in comment.subComment">
      <div class="text">{{ subComment.text }}</div>
      <div class="info">
        <span class="writer"><i class="far fa-user"></i> {{ subComment.writer }} </span>
        <span class="text_bar"> | </span>
        <span class="time">{{ subComment.time }}</span>
      </div>
    </div>
  </div>
</template>

<script>
import CommentWrite from '@/components/CommentWrite';
import vClickOutside from 'v-click-outside';

export default {
  // vClickOutside 컴포넌트 사용을 위해 필요한 부분
  directives: {
    clickOutside: vClickOutside.directive
  },
  components: {
    CommentWrite
  },
  props: {
    comment: Object
  },
  data() {
    return {
      toggle: false,
      subCommentWrite: "",
    }
  },
  methods: {
    initToggle() {
      this.toggle = false;
      this.subCommentWrite = "";
    },
    changeToggle() {
      this.toggle = !this.toggle;
      if (!this.toggle) {
        this.subCommentWrite = "";
      }
    }
  }
};
</script>

<style lang="scss" scoped>
$border-bottom: 1px solid #d9d9d9;
$active-color: #a7daed;
$gray-background-color: #f3f3f3;

.comment {
  font-size: 0.813rem;
  cursor: default;
  padding: 10px;
  border-bottom: $border-bottom;
  &:active {
    background-color: $active-color;
  }
  .info {
    font-size: 0.923em;
    color: #999;
  }
}

.sub_write {
  display: flex;
  font-size: 12px;
  border-bottom: $border-bottom;
  padding-left: 30px;
  background: #f9fafc url("../assets/ico_reply_gray.gif") no-repeat 17px 17px;
  > * {
    width: 100%;
    padding-right: 15px;
  }
}

.sud_comment {
  font-size: 0.813rem;
  cursor: default;
  border-bottom: $border-bottom;
  padding: 10px 10px 10px 35px;
  background: $gray-background-color url("../assets/ico_reply_gray.gif") no-repeat 17px 17px;
  .info {
    font-size: 0.923em;
    color: #999;
  }
}
</style>