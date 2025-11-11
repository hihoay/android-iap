. .taymay/discord.sh
get_current_tag() {
    tags=$(git ls-remote --tags origin | awk -F'/' '{print $NF}' | sort -V)
    latest_tag=$(echo "$tags" | tail -n 1)
    if [ -z "$latest_tag" ]; then
        latest_tag="1.0.0.0"
    fi
    echo $latest_tag
}
increase_tag() {
    branch_name=$1
    current_tag=$(get_current_tag)
    # nếu branch là release thì tăng lên 1
        # xoa ky tu tab dau dong
        version=$(echo $current_tag | sed 's/v\(.*\)/\1/')
        # echo $version
             # nếu branch là develop thì tăng lên 1 1.0.0.1
        if [ "$branch_name" == "develop" ]; then
            version=$(echo $version | awk -F. -v OFS=. '{$4++; print}')
        fi
        # nếu branch là develop thì tăng lên 1 1.0.1.0
        if [ "$branch_name" == "testing" ]; then
            version=$(echo $version | awk -F. -v OFS=. '{$3++; $4=0; print}')
        fi
        # nếu branch là release thì tăng lên 1 ở số thứ 2 1.1.0 còn số 3,4 là 0
        if [ "$branch_name" == "release" ]; then
            version=$(echo $version | awk -F. -v OFS=. '{$2++; $3=0; $4=0; print}')
        fi
        #nếu branch là main thì tăng lên 1 ở số thứ 1 2.0.0 còn số 2,3,4 là 0
        if [ "$branch_name" == "main" ]; then
            version=$(echo $version | awk -F. -v OFS=. '{$1++; $2=0; $3=0; $4=0; print}')
        fi
        version=$(echo $version | tr -d '\r')
        version=$(echo $version | sed 's/\n//g')
        new_tag="$version"
        new_tag=$(echo $new_tag | sed 's/\n//g')
        if [ $? -eq 0 ]; then
            echo $new_tag
        fi

}

auto_create_tag(){
  git fetch origin --tags
  if [ "$BRANCH_NAME" == "develop" ] || [ "$BRANCH_NAME" == "release" ] || [ "$BRANCH_NAME" == "main" ] || [ "$BRANCH_NAME" == "testing" ]; then
      git branch --show-current
      git fetch origin --tags
      ./gradlew publish
      if [ $? -ne 0 ]; then
          echo "Publish failed!"
          exit 1
      fi
      git tag -a  "$newTag" -m "[$BRANCH_NAME] Auto create tag "$newTag
      git push origin --tags
      message="$JOB_NAME [$newTag]($BUILD_URL/pipeline-console)"
      echo $message
      echo -e $message | send_discord_notification
  fi
}