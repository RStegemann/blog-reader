import {Component} from "react";

export default class BlogPostDetails extends Component{
    render(){
        const {blogPost} = this.props;
        return(
            <div>
                <h1><a href={blogPost.link}>{blogPost.title}</a></h1>
                <div dangerouslySetInnerHTML={{__html:blogPost.content}} />
            </div>
        );
    }
}