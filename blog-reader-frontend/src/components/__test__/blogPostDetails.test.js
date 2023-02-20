import {render, screen} from "@testing-library/react";
import BlogPostDetails from "../blogPostDetails";
import '@testing-library/jest-dom'

test('should render component', () =>{
    const post = {id:0, title:"title", excerpt:"excerpt", link:"link", content:"content", wordCounts:[["word", 1], ["otherWord", 2]]};
    render(<BlogPostDetails blogPost={post}/>);
    const appElement = screen.getByTestId('blogPostDetails');
    expect(appElement).toBeInTheDocument();
})