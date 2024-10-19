# 🎯 Branch Convention & Git Convention
## 🎯 Git Convention
- 🎉 **Start:** Start New Project - ':tada: Start: '
- ✨ **Feat:** 새로운 기능을 추가 - ':sparkles: Feat: '
- 🐛 **Fix:** 버그 수정 - ':bug: Fix: '
- 🎨 **Design:** CSS 등 사용자 UI 디자인 변경 ':art: Design: '
- ♻️ **Refactor:** 코드 리팩토링 ':recycle: Refactor: '
- 🔧 **Settings:** Changing configuration files ':wrench: Settings: '
- 🗃️ **Comment:** 필요한 주석 추가 및 변경 ':card_file_box: Comment: '
- ➕ **Dependency/Plugin:** Add a dependency/plugin ':heavy_plus_sign: Dependency/Plugin: '
- 📝 **Docs:** 문서 수정 ':memo: Docs: '
- 🔀 **Merge:** Merge branches ':twisted_rightwards_arrows: Merge: '
- 🚀 **Deploy:** Deploying stuff ':rocket: Deploy: '
- 🚚 **Rename:** 파일 혹은 폴더명을 수정하거나 옮기는 작업만인 경우 ':truck: Rename: '
- 🔥 **Remove:** 파일을 삭제하는 작업만 수행한 경우 ':fire: Remove: '
- ⏪️ **Revert:** 전 버전으로 롤백 ':rewind: Revert: '
## 🪴 Branch Convention (GitHub Flow)
- `main`: 배포 가능한 브랜치, 항상 배포 가능한 상태를 유지
- `feature/{description}`: 새로운 기능을 개발하는 브랜치
    - 예: `feature/add-login-page`
### Flow
1. `main` 브랜치에서 새로운 브랜치를 생성.
2. 작업을 완료하고 커밋 메시지에 맞게 커밋.
3. Pull Request를 생성 / 팀원들의 리뷰.
4. 리뷰가 완료되면 `main` 브랜치로 병합.
5. 병합 후, 필요시 배포.
   **예시**:
```bash
# 새로운 기능 개발
git checkout -b feature/add-login-page
# 작업 완료 후, main 브랜치로 병합
git checkout main
git pull origin main
git merge feature/add-login-page
git push origin main
```
