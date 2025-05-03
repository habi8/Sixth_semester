const mongoose = require('mongoose');
const connect = mongoose.connect('mongodb://127.0.0.1:27017/library_db');

connect.then(()=>{
    console.log("database connected");
}
)

.catch((err)=>{
    console.log("Not connected",err);
})

const userSchema = new mongoose.Schema({
    id: { type: Number, required: true, unique: true},
    name: { type: String, required: true },
    email: { type: String, required: true, unique: true },
    role: { type: String, required: true }
});

const bookSchema = new mongoose.Schema({
    id: { type: Number, required: true, unique: true},
    title: { type: String, required: true },
    author: String,
    isbn: String,
    copies: { type: Number},
    available_copies: { type: Number},
    borrow_count: {type: Number, default: 0},
    created_at: { type: Date, default: Date.now },
    updated_at: { type: Date, default: Date.now }
  });

  const loanSchema = new mongoose.Schema({
    id: { type: Number, required: true, unique: true},
    user_id: { type: String, required: true },
    book_id: { type: String, required: true },
    issue_date: { type: Date, default: Date.now },
    due_date: Date,
    return_date: Date,
    status: { type: String, default: 'none' },
    extension_count: { type: Number, default: 0}
  });

  const User = mongoose.model('User', userSchema);
  const Book = mongoose.model('Book', bookSchema);
  const Loan = mongoose.model('Loan', loanSchema);

  module.exports = {User,Book,Loan};