notify_discord() {
    local title="$1"
    local message="$2"
    local url="$3"
    local color="$4"

    # Đổi tên màu sang mã integer Discord
    case "$color" in
        green) color_code=3066993 ;;
        red) color_code=15158332 ;;
        blue) color_code=3447003 ;;
        yellow) color_code=16776960 ;;
        *) color_code=0 ;;
    esac

    # Gửi payload hợp lệ
    curl -s -X POST "$DISCORD_WEBHOOK" \
        -H "Content-Type: application/json" \
        -d "$(jq -n \
            --arg title "$title" \
            --arg description "$message" \
            --arg url "$url" \
            --argjson color "$color_code" \
            '{embeds: [{title: $title, description: $description, url: $url, color: $color}]}' )"
}