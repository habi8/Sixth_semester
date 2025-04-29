const express = require('express');//<--library
const path = require('path');//<--module
const {User,Book,Loan} = require('./config');//<__imports the models
const app = express();//<--creates express app

app.use(express.json());//middleware

app.post('/api/users', async (req, res) => {
    try {
      const { name, email, role } = req.body;
      const user = new User({ name, email, role });
      await user.save();
      res.status(201).json(user);
    } catch (err) {
      res.status(400).json({ error: err.message });
    }
  });

app.get('/api/users/:id', async(req,res)=>{
    const userId = req.params.id;
    try {
        const user = await User.findById(userId);
        if (!user) {
          return res.status(404).json({ error: 'User not found' });
        }
        res.json(user);
      } catch (err) {
        res.status(400).json({ error: 'Invalid ID format' });
      }
})

const port = 3000;
app.listen(port,()=>{  //<--starts the server 
    console.log(`Server running on port:${port}`);
})
