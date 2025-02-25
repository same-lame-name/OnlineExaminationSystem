START
├──> login.html (Public Entry Point)
│     ├──> [POST /login] -> home.html (Authenticated Landing Page)
│     │     ├──> [ADMIN ROLE]
│     │     │     └──> admin/dashboard.html
│     │     │           ├──> admin/exams.html (Proposed: VIEW/ADD/EDIT/DELETE exams)
│     │     │           │     ├──> [POST /admin/exam/add] -> Add new exam
│     │     │           │     ├──> [POST /admin/exam/edit] -> Edit exam
│     │     │           │     └──> [POST /admin/exam/delete] -> Delete exam
│     │     │           ├──> admin/questions.html (Proposed: ADD questions)
│     │     │           │     └──> [POST /admin/question/add] -> Add question
│     │     │           ├──> admin/results.html (Proposed: VIEW/SEARCH results)
│     │     │           ├──> admin/profile.html (Proposed: VIEW profile, Change password)
│     │     │           │     └──> [POST /admin/password] -> Update password
│     │     │           └──> [POST /logout] -> logout.html
│     │     ├──> [USER ROLE]
│     │     │     └──> user/exams.html (Proposed: VIEW exams, Purchase subscription)
│     │     │           ├──> user/exam-purchase.html (Proposed: Purchase subscription)
│     │     │           │     └──> [POST /user/exam/purchase] -> Purchase confirmation
│     │     │           ├──> user/exam-take.html (Proposed: Give examination)
│     │     │           │     └──> [POST /user/exam/submit] -> Submit exam
│     │     │           ├──> user/results.html (Proposed: VIEW results)
│     │     │           ├──> user/profile.html (Proposed: VIEW profile, Change password)
│     │     │           │     └──> [POST /user/password] -> Update password
│     │     │           └──> [POST /logout] -> logout.html
│     │     └──> [POST /logout] -> logout.html
│     └──> [GET /register] -> register.html
│           └──> [POST /register] -> login.html?registered=true
└──> logout.html (Logout Confirmation)
└──> [GET /login] -> login.html