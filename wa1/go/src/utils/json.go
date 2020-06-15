package utils

import (
	"bytes"
	"encoding/json"
	"io"
	"strings"
)

func StringJsonTo(s string, v ...interface{}) {
	ReaderJsonTo(strings.NewReader(s), v...)
}

func ByteJsonTo(b []byte, v ...interface{}) {
	ReaderJsonTo(bytes.NewReader(b), v...)
}

func ReaderJsonTo(reader io.Reader, si ...interface{}) {
	decoder := json.NewDecoder(reader)
	for _, v := range si {
		err := decoder.Decode(v)
		if err != nil {
			panic(err)
		}
	}
}

func TypeToJsonByte(si ...interface{}) []byte {
	buff := bytes.NewBuffer([]byte{})
	encoder := json.NewEncoder(buff)

	for _, v := range si {
		encoder.Encode(v)
	}

	return buff.Bytes()
}

/*type jorge struct {
	Age int
}

func main() {
	s := "{\n  \"age\":31\n}\n{\n  \"age\":33 \n}"

	t := jorge{}
	t2 := jorge{}
	StringJsonTo(s, &t,&t2)

	fmt.Printf("%d %d\n", t.Age, t2.Age)

	fmt.Print(string(typeToJsonByte(t,t2,t)))
}*/
