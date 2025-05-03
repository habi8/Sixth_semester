const express = require('express');//<--library
const path = require('path');//<--module
const {User,Book,Loan} = require('./config');//<__imports the models
const app = express();//<--creates express app
//const router = express.Router();

app.use(express.json());//middleware

function getRandomId() {
  return Math.floor(Math.random() * 9000) + 1000;
}

//User endpoints
app.post('/api/users', async (req, res) => {
    try {
      
      const { name, email, role } = req.body;
      const user = new User({ name, email, role });
      user.id = getRandomId();
      await user.save();
      res.status(201).json(user);
    } catch (err) {
      res.status(400).json({ error: err.message });
    }
  });

app.get('/api/users/:id', async(req,res)=>{
    const userId = req.params.id;
    try {
        const user = await User.findOne({id: userId});
        if (!user) {
          return res.status(404).json({ error: 'User not found' });
        }
        res.json(user);
      } catch (err) {
        res.status(400).json({ error: 'Invalid ID format' });
      }
})

//Books endpoints
app.post('/api/books', async (req, res) => {
  try {


    const { title,author,isbn,copies } = req.body;
    const book = new Book({ title, author, isbn, copies });
    book.id = getRandomId();
    await book.save();
    res.status(201).json(book);
  } catch (err) {
    res.status(400).json({ error: err.message });
  }
});

app.get('/api/books', async (req, res) => {
  const search = req.query.search; //<--api/books?id=123 is query parameneter

  try {
    let query = {};
    if (search) {
      query = {
        $or: [
          { title: { $regex: search, $options: 'i' } },  // 'i' = case-insensitive
          { author: { $regex: search, $options: 'i' } }
        ]
      };
    }

    const results = await Book.find(query);
    res.json(results);
  } catch (err) {
    res.status(500).json({ error: 'Database query failed' });
  }
});


app.get('/api/books/:id', async(req,res)=>{
  const bookId = req.params.id;
  try {
      const book = await Book.findOne({id:bookId});
      if (!book) {
        return res.status(404).json({ error: 'Book not found' });
      }
      res.json(book);
    } catch (err) {
      res.status(400).json({ error: 'Invalid ID format' });
    }
})

app.put('/api/books/:id', async (req, res) => {
  const bookId = req.params.id; 
  const { copies, available_copies } = req.body;

  try {
    const updatedBook = await Book.findOneAndUpdate(
      { id: parseInt(bookId) }, // Use { id: ... } if you're using a custom numeric id
      { copies, available_copies },
      { new: true }
    );

    if (!updatedBook) {
      return res.status(404).json({ error: 'Book not found' });
    }

    res.json(updatedBook);
  } catch (err) {
    res.status(500).json({ error: 'Update failed' });
  }
});

app.delete('/api/books/:id', async (req, res) => {
  const  bookId  = req.params.id;//<--api/books/123 is req body parameter

  try {
    const deletedBook = await Book.findOneAndDelete({id: bookId});

    if (!deletedBook) {
      return res.status(404).json({ error: 'Book not found' });
    }

    res.status(204).send(); 

  } catch (err) {
    res.status(400).json({ error: 'Invalid book ID' });
  }
});

//loan endpoint
app.post('/api/loans', async (req, res) => {
  const { user_id, book_id, due_date } = req.body;

  try {
    const book = await Book.findOne({ id: book_id }); // ✅ use 'id', not 'book_id' if your schema uses 'id'

    if (!book) {
      return res.status(404).json({ error: 'Book not found' });
    }

    if (book.available_copies > 0) {
      const loan = new Loan({ user_id, book_id, due_date });
      loan.id = getRandomId();
      loan.issue_date = new Date();
      loan.status = "ACTIVE";

      book.available_copies -= 1;
      book.borrow_count += 1;

      await book.save();
      await loan.save();

      res.status(201).json(loan);
    } else {
      res.status(400).json({ error: 'No copies available' });
    }
  } catch (err) {
    res.status(400).json({ error: err.message });
  }
});


app.post('/api/returns', async (req, res) => {
  const loanId = parseInt(req.body.loan_id); // or just req.body.id

  try {
    const loan = await Loan.findOne({ id: loanId });
    const bookid = loan.book_id;

    if (!loan) {
      return res.status(404).json({ error: 'Loan not found' });
    }

    const book = await Book.findOne({id: bookid});
    book.available_copies = book.available_copies + 1;

    loan.return_date = Date.now(); // current timestamp
    loan.status = 'RETURNED';

    await book.save();
    await loan.save();

    res.status(201).json(loan);
  } catch (err) {
    res.status(400).json({ error: err.message });
  }
});

app.get('/api/loans/:user_id',async(req,res)=>{
  const user_id = req.params.user_id;
  try {
    
    const loans = await Loan.find({ user_id: user_id });
    //console.log(loans);
   
    const bookIds = loans.map(loan => loan.book_id);
    const books = await Book.find({ id: { $in: bookIds } });

   
    const bookMap = {};
    books.forEach(book => {
      bookMap[book.id] = book;
    });

    const response = loans.map(loan => ({ //<--formatting
      id: loan.id,
      book: {
        id: loan.book_id,
        title: bookMap[loan.book_id]?.title ,
        author: bookMap[loan.book_id]?.author
      },
      issue_date: loan.issue_date,
      due_date: loan.due_date,
      return_date: loan.return_date,
      status: loan.status
    }));

    res.json(response);
  } catch (err) {
    res.status(500).json({ error: 'Failed to fetch user loans' });
  }
})

app.get('/api/loans/overdue', async (req, res) => {
  
  try {
    const today = new Date();

    const overdueLoans = await Loan.find(
     { due_date: { $lt: today } } 
    );

    const userIds = overdueLoans.map(loan => loan.user_id);
    const bookIds = overdueLoans.map(loan => loan.book_id);

    const users = await User.find({ id: { $in: userIds } });
    const books = await Book.find({ id: { $in: bookIds } });

    const userMap = {};
    users.forEach(user => userMap[user.id] = user);

    const bookMap = {};
    books.forEach(book => bookMap[book.id] = book);

    const response = overdueLoans.map(loan => {
      const dueDate = new Date(loan.due_date);
      const daysOverdue = Math.floor((today - dueDate) / (1000 * 60 * 60 * 24));

      return {
        id: loan.id,
        user: {
          id: loan.user_id,
          name: userMap[loan.user_id]?.name,
          email: userMap[loan.user_id]?.email
        },
        book: {
          id: loan.book_id,
          title: bookMap[loan.book_id]?.title,
          author: bookMap[loan.book_id]?.author
        },
        issue_date: loan.issue_date,
        due_date: loan.due_date,
        days_overdue: daysOverdue
      };
    });

    console.log("Hehe",response);
  
    res.json(overdueLoans);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Failed to fetch overdue loans' });
  }
});



app.put('/api/loans/:id/extend', async (req, res) => {
  try {
    const loanId = req.params.id;
    const { extension_days } = req.body;

    if (!extension_days || typeof extension_days !== 'number' || extension_days <= 0) {
      return res.status(400).json({ error: 'Invalid extension_days' });
    }

    const loan = await Loan.findOne({ id: parseInt(loanId) });

    if (!loan) {
      return res.status(404).json({ error: 'Loan not found' });
    }

    const originalDueDate = new Date(loan.due_date);
    const extendedDueDate = new Date(originalDueDate);
    extendedDueDate.setDate(extendedDueDate.getDate() + extension_days);

    // Update the loan
    loan.due_date = extendedDueDate;
    loan.extensions_count = (loan.extensions_count || 0) + 1;
    loan.status = 'ACTIVE';
    await loan.save();

    res.json({
      id: loan.id,
      user_id: loan.user_id,
      book_id: loan.book_id,
      issue_date: loan.issue_date,
      original_due_date: originalDueDate.toISOString(),
      extended_due_date: extendedDueDate.toISOString(),
      status: loan.status,
      extensions_count: loan.extensions_count
    });
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Failed to extend loan' });
  }
});

//Statistics endpoints
app.get('/api/stats/books/popular', async (req, res) => {
  try {
    const popularBooks = await Book.find({})
      .sort({ borrow_count: -1 }) // descending order
      .limit(2) // top 2
      .select({ id: 1, title: 1, author: 1, borrow_count: 1, _id: 0 }); // exclude MongoDB _id

    const response = popularBooks.map(book => ({
      book_id: book.id,
      title: book.title,
      author: book.author,
      borrow_count: book.borrow_count
    }));

    res.json(response);
  } catch (err) {
    res.status(500).json({ error: 'Failed to fetch popular books' });
  }
});

// app.get('/api/stats/users/active', async (req, res) => {
//   try {
//     const aggregated = await Loan.aggregate([
//       {
//         $group: {
//           _id: "$user_id",
//           books_borrowed: { $sum: 1 },
//           current_borrows: {
//             $sum: {
//               $cond: [{ $eq: ["$status", "ACTIVE"] }, 1, 0]
//             }
//           }
//         }
//       },
//       {
//         $sort: { books_borrowed: -1 }
//       },
//       {
//         $limit: 5 // or 2, depending on what you want
//       },
//       {
//         $lookup: {
//           from: "users",
//           localField: "_id",
//           foreignField: "id",
//           as: "user"
//         }
//       },
//       {
//         $unwind: "$user"
//       },
//       {
//         $project: {
//           _id: 0,
//           user_id: "$_id",
//           name: "$user.name",
//           books_borrowed: 1,
//           current_borrows: 1
//         }
//       }
//     ]);

//     res.json(aggregated);
//   } catch (err) {
//     res.status(500).json({ error: 'Failed to fetch active users' });
//   }
// });

app.get('/api/stats/overview', async (req, res) => {
  try {
    const todayStart = new Date();
    todayStart.setHours(0, 0, 0, 0);

    const todayEnd = new Date();
    todayEnd.setHours(23, 59, 59, 999);

    const [
      totalBooks,
      totalUsers,
      booksAvailable,
      booksBorrowed,
      overdueLoans,
      loansToday,
      returnsToday
    ] = await Promise.all([
      Book.countDocuments(),
      User.countDocuments(),
      Book.aggregate([
        { $group: { _id: null, total: { $sum: "$available_copies" } } }
      ]).then(res => res[0]?.total || 0),
      Loan.countDocuments({ status: "ACTIVE" }),
      Loan.countDocuments({
        status: "ACTIVE",
        due_date: { $lt: new Date() }
      }),
      Loan.countDocuments({
        issue_date: { $gte: todayStart, $lte: todayEnd }
      }),
      Loan.countDocuments({
        return_date: { $gte: todayStart, $lte: todayEnd }
      })
    ]);

    res.json({
      total_books: totalBooks,
      total_users: totalUsers,
      books_available: booksAvailable,
      books_borrowed: booksBorrowed,
      overdue_loans: overdueLoans,
      loans_today: loansToday,
      returns_today: returnsToday
    });
  } catch (err) {
    res.status(500).json({ error: "Failed to fetch overview statistics" });
  }
});


const port = 3000;
app.listen(port,()=>{  //<--starts the server 
    console.log(`Server running on port:${port}`);
})
