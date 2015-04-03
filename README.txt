Viterbi-decoded Maximum-Entropy Markov Model

It is a simple system to learn a Maximum-Entropy Markov Model for noun group chunking. 

I used a portion of the training data which is available from UPenn(ftp://ftp.cis.upenn.edu/pub/chunker/). This data is provided in a simple form, one word per line, with the word, its part of speech, and BIO tag on a line. Sentence boundaries are indicated by a blank line. The B tag is used only if a word begins a new noun group, and the previous word ends a noun group. Any other word at the beginning or inside a noun group is tagged with an I. The Penn data consists of 200K words training;  I used 100K lines of this for training, and 10K lines for test.

The learning tool is openNLP http://maxent.sourceforge.net/howto.html.

1.Features assignment.

I used seven features 
current word -- curWord
current POS tag -- curTag
previous word POS tag -- preTag
following word POS tag -- postTag 
previous and current word POS conjunction -- preConj
current word and following word POS conjunction -- postConj
previous word BIO State -- preState
value -- B I O

For example,
curWord=trade  curTag=NN  preTag=IN postTag=NNS  preConj=IN+NN  postConj=NN+NNS  preState=O  I


2.Processing training and test data

Using the features above, I processed the training and test data, please see the two files trainingProcessed.txt and testProcessed.txt in the bin directory


3.Create model with the trainingProcessed.txt

Using openNLP, I created the training model as the file Model.txt in the bin directory.


4.Predict the test data 

Based the model above, I predicted the test data with the features above, the output file is result.txt in the bin directory.


5. Compile and Run

Compile: change directory into the src directory, and input the following command.
	javac -cp ../lib/*:. -d ../bin *.java 
Run: change directory into the bin directory, and input the following command.
	java -cp ../lib/*:. MaxEnt


6..Evaluation--tag accuracy/NP precision/recall/F-measure

The running output will show the evaluation statistics.













