<template>
  <section>
    <input type="text" v-model="postDto.title" placeholder="제목을 입력하세요." required>
    <VueEditor ref="editor" v-model="postDto.content" useCustomImageHandler
               @image-added="handleImageAdded"
               @image-removed="handleImageRemoved"
               :editorToolbar="customToolbar"></VueEditor>
    <div class="btn_container">
      <div class="post_btn" @click="submit">등록</div>
      <div class="cancel_btn" @click="cancel">취소</div>
    </div>
  </section>
</template>

<script>
import { VueEditor, Quill } from "vue2-editor";
import { PostDto } from '@/dto/PostDto';

export default {
  components: {
    VueEditor
  },
  data() {
    const imageFiles = new Map();
    const postDto = new PostDto();
    return {
      postDto, // 데이터 전달 객체
      imageFiles,
      customToolbar: [
        [{ header: [false, 1, 2, 3, 4, 5, 6] }],
        [{ 'align': [] }],
        [{ list: "ordered" }, { list: "bullet" }, { list: "check" }],
        ["image", "video"]
      ],
    };
  },
  created() {
    // 수정하는 경우
    if (this.$route.query.postId !== undefined) {
      this.postDto = this.$store.state.postDto;
    }
  },
  methods: {
    handleImageAdded(file, Editor, cursorLocation, resetUploader) {
      const blobUrl = URL.createObjectURL(file);

      // blob 이미지가 안올라가는 문제 해결
      const Image = Quill.import('formats/image');
      Image.sanitize = url => url;
      // blob 이미지 url 추가
      Editor.insertEmbed(cursorLocation, 'image', blobUrl);
      // 커서를 맨뒤로 이동하고 다음줄로 넘어감
      Editor.setSelection(Editor.getSelection().index + 1);
      Editor.insertText(Editor.getSelection().index, '\n');
      resetUploader();
      this.imageFiles.set(blobUrl, file);
    },
    handleImageRemoved(blobUrl) {
      this.imageFiles.delete(blobUrl);
    },
    async submit() {
      if (this.postDto.title === "") {
        alert("제목을 입력하세요.");
        return;
      }
      if (this.postDto.content === "") {
        alert("내용을 입력하세요")
        return;
      }
      const isPost = confirm("게시글을 등록하시겠습니까?");
      if (isPost) {
        let content = this.postDto.content;

        const formData = new FormData();
        for (let [key, value] of this.imageFiles) {
          let type = `.${value.name.split('.')[1]}`; // 이미지 타입
          let imageName = key + type;
          content = content.replace(key, imageName);
          formData.append("imageFile", value, imageName);
          formData.append("images", imageName);

        }
        formData.append("title", this.postDto.title);
        formData.append("content", content);

        try {
          if (this.$route.query.postId !== undefined) {
            // 수정하는 경우
            formData.append("id", this.postDto.id);
            formData.append("writer", this.postDto.writer);
            await this.$store.dispatch('UPDATE_POST_DATA', formData);
          } else {
            // 새로 게시글 작성하는 경우
            await this.$store.dispatch('UPLOAD_POST_DATA', formData);
          }
        } catch (error) {
          alert("게시글 등록에 실패했습니다.");
        } finally {
          await this.$router.push("/user")
        }
      }
    },
    cancel() {
      const isCancel = confirm("글쓰기를 취소하시겠습니까?");
      if (isCancel) {
        this.$router.push("/user");
      }
    }
  }
};
</script>

<style lang="scss" scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR&display=swap');

$button-color: #1a72e6;
$button-hover-color: #4383ee;
$cancel-hover-color: #f1f1f1;
$active-color: #a7daed;

section {
  font-family: 'Noto Sans KR', sans-serif;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 24px;
  > * {
    width: 100%;
    max-width: 1200px;
  }
  input {
    padding: 6px 4px 6px 8px;
    margin-bottom: 24px;
    height: 38px;
  }
  #editor {
    height: 450px;
  }
  .btn_container {
    margin-top: 24px;
    display: flex;
    justify-content: center;
    align-items: center;
    > * {
      cursor: pointer;
      font-size: 20px;
      padding: 8px 56px;
      margin: 0 16px;
      border-radius: 4px;
      border: 1px solid;
    }
    .post_btn {
      background-color: $button-color;
      color: white;
      border-color: $button-color;
      &:hover, &:active {
        background-color: $button-hover-color;
        border-color: $button-hover-color;
      }
    }
    .cancel_btn {
      &:hover {
        background-color: $cancel-hover-color;
      }
    }
  }
}

@media screen and (max-width: 768px) {
  section {
    font-size: 14px;
    padding: 16px 12px;
    input {
      margin-bottom: 16px;
    }
    .btn_container {
      margin-top: 16px;
      > * {
        font-size: inherit;
        padding: 6px 0;
        margin: 0;
        flex: 1 1 50%;
        text-align: center;
      }
      .post_btn {
        margin-right: 6px;
      }
      .cancel_btn {
        margin-left: 6px;
        &:hover {
          background-color: white;
        }
        &:active {
          background-color: $active-color
        }
      }
    }
  }
}
</style>