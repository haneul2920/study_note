import os
import datetime
import subprocess
import shutil

# 설정
GIT_REPO_PATH = r"C:\Users\admin\Desktop\개인노트\study_note"  # 저장소 경로
LEARNING_FOLDER_PATH = r"C:\Users\admin\Desktop\개인노트"  # 학습 파일 저장 폴더
FILENAME_FORMAT = "{date}"  # 또는 "{date}.md"
GIT_USERNAME = "haneul2920"
GIT_EMAIL = "kge2920@gmail.com"

def get_today_file_path():
    today_str = datetime.date.today().strftime("%Y-%m-%d")
    filename = FILENAME_FORMAT.format(date=today_str)
    return os.path.join(LEARNING_FOLDER_PATH, filename)

def is_file_valid(file_path):
    return os.path.exists(file_path) and os.path.getsize(file_path) > 10

def copy_learning_file(src_path):
    filename = os.path.basename(src_path)
    dest_path = os.path.join(GIT_REPO_PATH, filename)
    shutil.copyfile(src_path, dest_path)
    print(f"✅ 파일 복사 완료: {filename}")
    return filename

def commit_and_push():
    os.chdir(GIT_REPO_PATH)
    subprocess.run(["git", "config", "user.name", GIT_USERNAME])
    subprocess.run(["git", "config", "user.email", GIT_EMAIL])
    subprocess.run(["git", "add", "."])
    commit_msg = f"✅ [자동 업로드] {datetime.date.today()} 학습 내용"
    subprocess.run(["git", "commit", "-m", commit_msg])
    subprocess.run(["git", "push", "origin", "main"])
    print("🚀 GitHub 푸시 완료")

if __name__ == "__main__":
    today_file = get_today_file_path()
    if is_file_valid(today_file):
        copy_learning_file(today_file)
        commit_and_push()
    else:
        print("오늘 학습 파일이 없거나 내용이 부족하여 업로드하지 않습니다.")
