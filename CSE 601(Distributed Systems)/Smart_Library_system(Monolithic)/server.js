const express = require('express');//<--library
const path = require('path');//<--module
const {User,Book,Loan} = require('./config');//<__imports the models
const app = express();//<--creates express app

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
app.post('/api/loans',async(req,res)=>{
  const {user_id, book_id, due_date} = req.body;

  try{
      const loan = new Loan({ user_id, book_id, due_date });
      loan.id = getRandomId();
      loan.issue_date = Date.now();
      loan.status = "ACTIVE";
      await loan.save();
      res.status(201).json(loan);  
    }
    catch (err) {
      res.status(400).json({ error: err.message });
    }
})

app.post('/api/returns', async (req, res) => {
  const loanId = parseInt(req.body.loan_id); // or just req.body.id

  try {
    const loan = await Loan.findOne({ id: loanId });

    if (!loan) {
      return res.status(404).json({ error: 'Loan not found' });
    }

    loan.return_date = Date.now(); // current timestamp
    loan.status = 'RETURNED';

    await loan.save();

    res.status(201).json(loan);
  } catch (err) {
    res.status(400).json({ error: err.message });
  }
});




const port = 3000;
app.listen(port,()=>{  //<--starts the server 
    console.log(`Server running on port:${port}`);
})
