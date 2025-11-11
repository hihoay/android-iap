upload_file() {
    local file_path="$1"
    local file_name="$2"
    echo $(curl -F "file=@\"$file_path\";filename=\"$file_name\"" "$FILE_SERVICE/upload")
}