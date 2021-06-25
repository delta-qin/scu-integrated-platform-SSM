// db = db.getSiblingDB("admin");
// db.auth("deltaqin", "deltaqinPASSWORD");
db.createUser({
    user: "deltaqin1",
    pwd: "deltaqinPASSWORD",
    roles: [
        {
            role: "readWrite",
            db: "commentdb"
        }
    ]
});
