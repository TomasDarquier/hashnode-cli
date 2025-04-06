# Hashnode CLI

An unofficial, powerful Java-based command-line interface tool that enables you to manage your Hashnode blog directly from your terminal. Create and publish blog posts and series with ease, while keeping your API credentials secure and encrypted.

> **Disclaimer:** This is an unofficial tool and is not affiliated with, officially connected to, or endorsed by Hashnode. This project is maintained independently by the community.

## Features

- 🚀 Create and publish blog posts directly from the terminal
- 📚 Manage blog series efficiently
- 🔐 Secure API key storage with password encryption
- ⚡ Streamlined workflow for content creation
- 🛠️ Simple and intuitive command structure
- 📝 Support for Markdown files
- 🔄 Multiple session management

## Installation

The following installation steps are for ZSH. For other shells, adjust the commands accordingly:

```bash
# Clone the repository
cd ~
mkdir hashnode-cli
git clone https://github.com/TomasDarquier/hashnode-cli.git ~/hashnode-cli

# Build the project
cd ~/hashnode-cli
mvn clean package

# Add to your shell configuration
echo "alias hashnode-cli='java -jar ~/hashnode-cli/target/hashnode-cli-1.0-SNAPSHOT-jar-with-dependencies.jar'" >> ~/.zshrc
source ~/.zshrc
```

## Getting Started

### Initial Setup

Before creating your first post, you need to set up a session with your Hashnode API token:

```bash
hashnode-cli create session -n <session-name> -t <api-token>
```

You'll be prompted to create a password to encrypt your API token. **Important:** Remember this password as you'll need it each time you create a new post.

### Command Structure

The CLI follows this command pattern:

```bash
hashnode-cli <action> <resource> [options]
```

Where:
- `action`: The operation to perform (create, list, delete)
- `resource`: The type of resource (post, series, session)
- `options`: Additional parameters for the command

## Common Commands

### Managing Sessions

```bash
# Create a new session
hashnode-cli create session -n <session-name> -t <api-token>

# List all sessions
hashnode-cli list sessions
```

### Creating Posts

```bash
# Create a post from a Markdown file
hashnode-cli create post -c <markdown-file> -t <title> [options]

# Additional options:
#   --subtitle    Add a subtitle to your post
#   --banner      Add a banner image to your post
```

When creating a post, you'll be guided through an interactive process to:
1. Select your session
2. Enter your encryption password
3. Choose your blog
4. Optionally select or create a series

### Managing Series

```bash
# Create a new series
hashnode-cli create series -n <series-name> -d <description>

# List all series
hashnode-cli list series
```

## Security

Your API tokens are:
- Stored locally with strong encryption
- Protected by your personal password
- Securely managed for multiple sessions

## Best Practices

1. **Markdown Files**: Prepare your blog posts in Markdown format
2. **Session Management**: Create different sessions for different blogs
3. **Password Security**: Use a strong password for API token encryption
4. **Regular Backups**: Keep backups of your Markdown files

## Contributing

We welcome contributions to make this tool even more powerful! Here's how you can help:

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

[MIT License](LICENSE) - Feel free to use and modify this tool for your needs.

## Support

If you encounter any issues or have suggestions, please:
1. Check the [Issues](https://github.com/TomasDarquier/hashnode-cli/issues) section
2. Create a new issue if needed
3. Provide detailed information about the problem

---