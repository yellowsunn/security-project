# build stage
FROM node:14.15-alpine as build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install --production
COPY . .
RUN npm run build

# production stage
FROM nginx:1.18.0-alpine as production-stage
  # 새로고침시 404 문제 해결
COPY default.conf /etc/nginx/conf.d/default.conf
COPY --from=build-stage /app/dist /usr/share/nginx/html

  # 환경 변수 설정
COPY entrypoint.sh /usr/share/nginx/
ENTRYPOINT ["/usr/share/nginx/entrypoint.sh"]

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
