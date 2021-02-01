export class SimplePostDto {
  constructor(content) {
    this.id = content.id;
    this.no = content.no;
    this.title = content.title;
    this.commentSize = content.commentSize;
    this.writer = content.writer;
    this.hit = content.hit;
    this.time = content.time;
    this.hasImage = content.hasImage;
  }
}