import {render, screen} from "@testing-library/react";
import '@testing-library/jest-dom'
import BlogPostSelector from "../blogPostSelector";

test('should render component', () =>{
    const post = {id:0, title:"title", excerpt:"excerpt", link:"link", content:"content", wordCounts:[["word", 1], ["otherWord", 2]]};
    render(<BlogPostSelector
        key={post.id}
        blogPost={post}
        index = {0}
        />);
    const appElement = screen.getByTestId('postListItem-0');
    expect(appElement).toBeInTheDocument();
})