<!-- 게시글이 보이는 콘텐츠 -->
<template>
  <section>
    <div class="main_title">
      <div class="text" @click="$router.push('/user')">유저게시판</div>
    </div>
    <div class="title_info">
      <div class="title">{{ postData.title }}</div>
      <div class="info">
        <span class="writer"><i class="far fa-user"></i> {{ postData.writer }} </span>
        <span class="text_bar"> | </span>
        <span class="time">날짜 {{ postData.postTime }}</span>
        <span class="text-bar"> | </span>
        <span class="hit">조회 {{ postData.hit }}</span>
      </div>
    </div>
    <div class="content_area">
      <div class="content" v-html="postData.content"></div>
      <div class="btns_area">
        <div class="list_btn" @click="$router.push('/user')">목록보기</div>
        <div class="empty_box" v-if="currentUser === postData.writer"></div>
        <div class="update_btn" v-if="currentUser === postData.writer">수정</div>
        <div class="delete_btn" v-if="currentUser === postData.writer">삭제</div>
      </div>
    </div>
    <div class="comment_container">
      <div class="title">
        <div @click="getCommentData"><span>댓글</span><span>{{ commentData.totalElements }}</span><i class="fas fa-sync"></i></div>
      </div>
      <Comment :comment="comment" v-for="comment in commentData.content" :key="comment.commentId"></Comment>
    </div>

    <infinite-loading :identifier="infiniteId" @infinite="infiniteHandler" spinner="spiral">
      <span slot="no-more"></span>
      <span slot="no-results"></span>
    </infinite-loading>

    <CommentWrite v-on:initWriteComment="initWriteComment" :writeComment="writeComment">
      <textarea :value="writeComment" @input="writeComment = $event.target.value" placeholder="댓글을 입력해 주세요."></textarea>
    </CommentWrite>
  </section>
</template>

<script>
import CommentWrite from '@/components/CommentWrite'
import Comment from '@/components/Comment'

export default {
  components: {
    Comment,
    CommentWrite,
  },
  data() {
    return {
      writeComment: ''
    }
  },
  computed: {
    postData() {
      return this.$store.state.postDto;
    },
    currentUser() {
      return this.$store.state.userInfo.username;
    },
    commentData() {
      return this.$store.state.commentDto;
    },
    infiniteId() {
      return this.$store.state.infiniteId;
    },
    page() {
      return this.$store.state.page;
    }
  },
  created() {
    this.$store.state.page = 0;
    this.$store.state.infiniteId = +new Date();
  },
  methods: {
    // 댓글 불러오기
    async getCommentData() {
      await this.$store.dispatch('GET_COMMENT_DATA', { postId: this.$route.params.postId });
      this.$store.state.page = 0;
      this.$store.state.infiniteId += 1; // 무한 스크롤 다시 작동
    },
    // 댓글을 업로드한경우 내용 초기화
    initWriteComment() {
      this.writeComment = "";
    },
    async infiniteHandler($state) {
      await this.$store.dispatch('GET_COMMENT_DATA', {
        postId: this.$route.params.postId,
        page: this.page
      });

      if (this.commentData.last) {
        $state.complete();
      } else {
        this.$store.state.page += 1;
        $state.loaded();
      }
    }
  },

};
</script>

<style lang="scss" scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500&display=swap');

$border-color: #9ea0ab;
$border-bottom: 1px solid #d9d9d9;
$active-color: #a7daed;
$default-padding: 10px;

p {
  margin: 0;
  color: #1a72e6;
}

section {
  font-size: 14px;
  font-family: 'Noto Sans KR', sans-serif;
  .main_title {
    font-size: 24px;
    font-weight: 500;
    margin-bottom: 24px;
    width: 100%;
    max-width: 1200px;
    .text {
      cursor: pointer;
    }
  }
  .title_info {
    margin-top: 2px;
    padding: 10px;
    border-bottom: $border-bottom;
    .title {
      display: flex;
      align-items: center;
      font-size: 16px;
      font-weight: bold;
      margin-bottom: 5px;
      .comment {
        padding-left: 6px;
        font-weight: bold;
        font-size: 13px;
        color: #4376f3;
      }
    }
    .info {
      font-size: 13px;
      color: #999;
    }
  }
  .content_area {
    border-bottom: $border-bottom;
    .content {
      padding: 20px 10px;
      min-height: 250px;
      ::v-deep p {
        margin-bottom: 0;
        font-size: 14px;
        padding: 2px 0;
      }
      ::v-deep img {
        display: block;
        max-width: 100%;
      }

    }
    .btns_area {
      display: flex;
      justify-content: flex-start;
      font-size: 14px;
      text-align: center;
      > * {
        flex: 1 1 25%;
      }
      .list_btn, .update_btn, .delete_btn {
        cursor: pointer;
        margin-bottom: 10px;
        text-align: center;
        padding: 6px 0;
        border: 1px solid $border-color;
        border-radius: 2px;
        margin-right: 10px;
        &:active {
          background-color: $active-color;
        }
      }
      .list_btn {
        margin-left: 10px;
      }
    }
  }
  .comment_container {
    .title {
      padding: $default-padding;
      border-bottom: $border-bottom;
      display: flex;
      div {
        cursor: pointer;
        &:active {
          background: $active-color;
        }
      }
      * {
        margin-right: 4px;
      }
    }

  }
}

@media screen and (max-width: 768px) {
  section {
    .main_title {
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 16px;
      margin-bottom: 0;
      padding: 12px;
      box-shadow: 0 1px 3px 0 #bdbdbd;

      .text {
        &:active {
          background-color: $active-color;
        }
      }
    }
    .content_area {
      .content {
        // 동영상 비율
        ::v-deep iframe {
          width: 100%;
          height: 400px;
        }
      }
    }
  }

}

@media screen and (max-width: 30rem) {
  section {
    .content_area {
      .content {
        // 동영상 비율
        ::v-deep iframe {
          width: 100%;
          height: 200px;
        }
      }
    }
  }
}
</style>