import {render, screen} from "@testing-library/react";
import '@testing-library/jest-dom'
import BlogPostList from "../blogPostList";

test('should render component', () =>{
    const posts = [
        {id:0, title:"title", excerpt:"excerpt", link:"link", content:"content", wordCounts:[["word", 1], ["otherWord", 2]]},
        {id:1, title:"title", excerpt:"excerpt", link:"link", content:"content", wordCounts:[["word", 1], ["otherWord", 2]]},
        {id:2, title:"title", excerpt:"excerpt", link:"link", content:"content", wordCounts:[["word", 1], ["otherWord", 2]]},
        {id:3, title:"title", excerpt:"excerpt", link:"link", content:"content", wordCounts:[["word", 1], ["otherWord", 2]]}
    ]
    render(<BlogPostList blogPosts={posts}/>);
    const listItem0 = screen.getByTestId('postListItem-0');
    const listItem1 = screen.getByTestId('postListItem-1');
    const listItem2 = screen.getByTestId('postListItem-2');
    const listItem3 = screen.getByTestId('postListItem-3');
    expect(listItem0).toBeInTheDocument();
    expect(listItem1).toBeInTheDocument();
    expect(listItem2).toBeInTheDocument();
    expect(listItem3).toBeInTheDocument();
})