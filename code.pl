
:- consult(mini).

result(SOld,A,SNew).

%moving north with no pokemon
at(X,Y,E,P,S,A,TH):-
	at(X,Y2,E2,P,S1,_,TH1),
	result(S1,A,S),
	TH>0,
	TH is TH1-1,
	A = n,
	\+  wall(X,Y2,X,Y),
	Y2 is Y-1,
	E >0,
	E2 is E-1,
	\+ poky(X,Y,S1).

%moving south with no pokemon
at(X,Y,E,P,S,A,TH):-
	at(X,Y2,E2,P,S1,_,TH1),
	result(S1,A,S),
	TH>0,
	TH is TH1-1,
	A = s,
	\+  wall(X,Y2,X,Y),
	Y2 is Y+1,
	E >0,
	E2 is E-1,
	\+ poky(X,Y,S1).

%moving east with no pokemon
at(X,Y,E,P,S,A,TH):-
	at(X2,Y,E2,P,S1,_,TH1),
	result(S1,A,S),
	TH>0,
	TH is TH1-1,
	A = e,
	\+  wall(X,Y2,X,Y),
	X2 is X-1,
	E >0,
	E2 is E-1,
	\+ poky(X,Y,S1).

%moving west with no pokemon
at(X,Y,E,P,S,A,TH):-
	at(X2,Y,E2,P,S1,_,TH1),
	result(S1,A,S),
	TH>0,
	TH is TH1-1,
	A = w,
	\+  wall(X,Y2,X,Y),
	X2 is X+1,
	E >0,
	E2 is E-1,
	\+ poky(X,Y,S1).

%moving north with pokemon
at(X,Y,E,P,S,A,TH):-
	at(X,Y2,E2,P1,S1,_,TH1),
	result(S1,A,S),
	TH>0,
	TH is TH1-1,
	A = n,
	\+  wall(X,Y2,X,Y),
	Y2 is Y-1,
	E >0,
	E2 is E-1,
	poky(X,Y,S1),
	\+ collected(X,Y,_),
	P is P1-1.

%moving south with pokemon
at(X,Y,E,P,S,A,TH):-
	at(X,Y2,E2,P1,S1,_,TH1),
	result(S1,A,S),
	TH>0,
	TH is TH1-1,
	A = s,
	\+  wall(X,Y2,X,Y),
	Y2 is Y+1,
	E >0,
	E2 is E-1,
	poky(X,Y,S1),
	\+ collected(X,Y,_),
	P is P1-1.

%moving east with pokemon
at(X,Y,E,P,S,A,TH):-
	at(X2,Y,E2,P1,S1,_,TH1),
	result(S1,A,S),
	TH>0,
	TH is TH1-1,
	A = e,
	\+  wall(X,Y2,X,Y),
	X2 is X-1,
	E >0,
	E2 is E-1,
	poky(X,Y,S1),
	\+ collected(X,Y,_),
	P is P1-1.

%moving west with pokemon
at(X,Y,E,P,S,A,TH):-
	at(X2,Y,E2,P1,S1,_,TH1),
	result(S1,A,S),
	TH>0,
	TH is TH1-1,
	A = w,
	\+  wall(X,Y2,X,Y),
	X2 is X+1,
	E >0,
	E2 is E-1,
	poky(X,Y,S1),
	\+ collected(X,Y,_),
	P is P1-1.

collected(X,Y,S):-
	collected(X,Y,s0),
	\+ at(X,Y,_,_,S,_,_).