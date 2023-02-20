import {render, screen} from "@testing-library/react";
import '@testing-library/jest-dom'
import BlogPostList from "../blogPostList";
import WordTable from "../wordTable";

test('should render component', () =>{
    const wordCounts =[
        ["word1" , 2],
        ["word2" , 3],
        ["word3" , 2],
        ["word4" , 4],
    ]
    render(<WordTable wordCountArray={wordCounts}/>);
    const listItem0 = screen.getByTestId('wordCell-word1');
    const listItem1 = screen.getByTestId('wordCell-word2');
    const listItem2 = screen.getByTestId('wordCell-word3');
    const listItem3 = screen.getByTestId('wordCell-word4');
    const listItem4 = screen.getByTestId('countCell-word1');
    const listItem5 = screen.getByTestId('countCell-word2');
    const listItem6 = screen.getByTestId('countCell-word3');
    const listItem7 = screen.getByTestId('countCell-word4');
    expect(listItem0).toBeInTheDocument();
    expect(listItem1).toBeInTheDocument();
    expect(listItem2).toBeInTheDocument();
    expect(listItem3).toBeInTheDocument();
    expect(listItem4).toBeInTheDocument();
    expect(listItem5).toBeInTheDocument();
    expect(listItem6).toBeInTheDocument();
    expect(listItem7).toBeInTheDocument();
})