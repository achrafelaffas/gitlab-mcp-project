import type React from "react";
import {useState, useRef, useEffect} from "react";
import axios from "axios";

import Markdown from 'markdown-to-jsx';



import {Button} from "@/components/ui/button";
import {Input} from "@/components/ui/input";
import {Card, CardContent, CardHeader, CardTitle} from "@/components/ui/card";
import {ScrollArea} from "@/components/ui/scroll-area";
import {Avatar, AvatarFallback} from "@/components/ui/avatar";
import {Send, Bot, User, Loader2, RotateCcw} from "lucide-react";

interface Message {
  sender: "user" | "ai";
  text: string;
}

export default function ChatInterface() {
  const [messages, setMessages] = useState<Message[]>([]);
  const [input, setInput] = useState<string>("");
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(false);
  const scrollAreaRef = useRef<HTMLDivElement>(null);
  const [isAtBottom, setIsAtBottom] = useState(true);

  const sendMessage = async () => {
    if (!input.trim()) return;

    const userMessage: Message = {sender: "user", text: input};
    setMessages((prev) => [...prev, userMessage]);
    setInput("");
    setIsLoading(true);
    setError(false);

    try {
      const response = await axios.post(`http://localhost:8080/chat/send?message=${userMessage.text}`);
      const aiMessage: Message = {sender: "ai", text: response.data};
      setMessages((prev) => [...prev, aiMessage]);
    } catch (err) {
      console.error(err);
      setMessages((prev) => [
        ...prev,
        {sender: "ai", text: "Sorry, something went wrong."},
      ]);
      setError(true);
    } finally {
      setIsLoading(false);
    }
  };

  const handleScroll = (event: React.UIEvent<HTMLDivElement>) => {
    const {scrollTop, scrollHeight, clientHeight} = event.currentTarget;
    setIsAtBottom(scrollHeight - scrollTop - clientHeight < 50);
  };

  const onSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (input.trim()) {
      sendMessage();
      setIsAtBottom(true);
    }
  };

  useEffect(() => {
    if (isAtBottom && scrollAreaRef.current) {
      const scrollContainer = scrollAreaRef.current.querySelector("[data-radix-scroll-area-viewport]");
      if (scrollContainer) {
        scrollContainer.scrollTop = scrollContainer.scrollHeight;
      }
    }
  }, [messages, isAtBottom]);

  return (

    <Card className="w-full flex flex-col h-screen">
      <CardHeader className="border-b bg-white/50 backdrop-blur-sm">
        <CardTitle className="flex items-center gap-2">
          <Bot className="h-6 w-6 text-blue-600"/>
          AI Chat Assistant
        </CardTitle>
      </CardHeader>

      <CardContent className="flex-1 p-0 flex flex-col">
        <ScrollArea ref={scrollAreaRef} className="flex-1 p-4" onScrollCapture={handleScroll}>
          <div className="space-y-4">
            {messages.length === 0 && (
              <div className="text-center text-muted-foreground py-8">
                <Bot className="h-12 w-12 mx-auto mb-4 text-blue-600/50"/>
                <p className="text-lg font-medium">Welcome to AI Chat!</p>
                <p className="text-sm">Start a conversation by typing a message below.</p>
              </div>
            )}

            {messages.map((message, idx) => (
              <div
                key={idx}
                className={`flex gap-3 ${message.sender === "user" ? "justify-end" : "justify-start"}`}
              >
                {message.sender === "ai" && (
                  <Avatar className="h-8 w-8 bg-blue-600">
                    <AvatarFallback>
                      <Bot className="h-4 w-4 text-white"/>
                    </AvatarFallback>
                  </Avatar>
                )}

                <div
                  className={`rounded-lg px-4 py-2 ${
                    message.sender === "user" ? "bg-blue-600 text-white" : "bg-white border shadow-sm"
                  }`}
                >
                  {message.sender === "ai" ? (
                    <div className="text-left prose prose-sm max-w-none">
                      <Markdown>
                        {message.text}
                      </Markdown>
                    </div>
                  ) : (
                    <div className="whitespace-pre-wrap break-words">
                      <div className="text-left prose prose-sm max-w-none">
                        <Markdown>
                          {message.text}
                        </Markdown>
                      </div>

                    </div>
                  )}
                </div>

                {message.sender === "user" && (
                  <Avatar className="h-8 w-8 bg-slate-600">
                    <AvatarFallback>
                      <User className="h-4 w-4 text-white"/>
                    </AvatarFallback>
                  </Avatar>
                )}
              </div>
            ))}

            {isLoading && (
              <div className="flex gap-3 justify-start">
                <Avatar className="h-8 w-8 bg-blue-600">
                  <AvatarFallback>
                    <Bot className="h-4 w-4 text-white"/>
                  </AvatarFallback>
                </Avatar>
                <div className="bg-white border shadow-sm rounded-lg px-4 py-2">
                  <div className="flex items-center gap-2 text-muted-foreground">
                    <Loader2 className="h-4 w-4 animate-spin"/>
                    <span>AI is thinking...</span>
                  </div>
                </div>
              </div>
            )}
          </div>
        </ScrollArea>

        {error && (
          <div className="px-4 py-2 bg-red-50 border-t border-red-200">
            <div className="flex items-center justify-between">
              <p className="text-sm text-red-600">Something went wrong. Please try again.</p>
              <Button
                variant="outline"
                size="sm"
                onClick={sendMessage}
                className="text-red-600 border-red-200 hover:bg-red-50"
              >
                <RotateCcw className="h-4 w-4 mr-1"/>
                Retry
              </Button>
            </div>
          </div>
        )}

        <div className="border-t bg-white/50 backdrop-blur-sm p-4">
          <form onSubmit={onSubmit} className="flex gap-2">
            <Input
              value={input}
              onChange={(e) => setInput(e.target.value)}
              placeholder="Type your message..."
              disabled={isLoading}
              className="flex-1"
              autoFocus
            />
            <Button type="submit" disabled={!input.trim() || isLoading} size="icon" className="shrink-0">
              <Send className="h-4 w-4"/>
            </Button>
          </form>
          <p className="text-xs text-muted-foreground mt-2 text-center">
            Press Enter to send • AI responses are generated and may contain errors
          </p>
        </div>
      </CardContent>
    </Card>
  );
}
