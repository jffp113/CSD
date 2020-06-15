FROM sconecuratedimages/crosscompilers
RUN apk update
RUN apk add go git curl
COPY ./src /root/go/src/SGX_CSD_Server/src
RUN go get ./...
RUN go build  -compiler gccgo -buildmode=exe /root/go/src/SGX_CSD_Server/src/main.go

FROM alpine:latest

COPY --from=0 /opt/scone/cross-compiler/x86_64-linux-musl/lib/libgo.so.14 /opt/scone/lib/libgo.so.14
COPY --from=0 /opt/scone/cross-compiler/x86_64-linux-musl/lib/libgcc_s.so.1 /opt/scone/lib/libgcc_s.so.1
COPY --from=0 /opt/scone/lib/ld-scone-x86_64.so.1 /opt/scone/lib/ld-scone-x86_64.so.1
COPY --from=0 /opt/scone/cross-compiler/x86_64-linux-musl/lib/libc.scone-x86_64.so.1 /opt/scone/lib/libc.scone-x86_64.so.1
COPY --from=0 /etc/sgx-musl.conf /etc/sgx-musl.conf

COPY ./conf.json .
COPY --from=0 ./main .
CMD ["./main"]