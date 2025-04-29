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
    name: { type: String, required: true },
    email: { type: String, required: true, unique: true },
    role: { type: String, required: true }
});

const bookSchema = new mongoose.Schema({
    title: { type: String, required: true },
    author: String,
    isbn: String,
    availableCopies: { type: Number, default: 1 }
  });

  const loanSchema = new mongoose.Schema({
    userId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
    bookId: { type: mongoose.Schema.Types.ObjectId, ref: 'Book', required: true },
    issueDate: { type: Date, default: Date.now },
    returnDate: Date,
    status: { type: String, enum: ['issued', 'returned'], default: 'issued' }
  });

  const User = mongoose.model('User', userSchema);
  const Book = mongoose.model('Book', bookSchema);
  const Loan = mongoose.model('Loan', loanSchema);

  module.exports = {User,Book,Loan};